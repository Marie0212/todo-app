package de.todoapp.persistence;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteTaskRepository implements TaskWriter, TaskReader, TaskUpdater, TaskDeleter {

    private final String url;

    public SQLiteTaskRepository(String url) {
        this.url = url;
        System.out.println(">>> SQLiteTaskRepository AKTIV: " + url);
        init();
    }

    private void init() {
        // LocalDate speichern wir als ISO-String (YYYY-MM-DD) in TEXT
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
              id          INTEGER PRIMARY KEY,
              title       TEXT NOT NULL,
              description TEXT,
              due_date    TEXT,
              status      TEXT NOT NULL,
              category_id INTEGER
            );
            """;
        try (Connection c = DriverManager.getConnection(url);
             Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    @Override
    public Task save(Task task) {
        // InMemory.save überschreibt immer -> wir machen Upsert
        String sql = """
            INSERT INTO tasks(id, title, description, due_date, status, category_id)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
              title=excluded.title,
              description=excluded.description,
              due_date=excluded.due_date,
              status=excluded.status,
              category_id=excluded.category_id
            """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql)) {

            bindTask(ps, task);
            ps.executeUpdate();
            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Save failed", e);
        }
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT id, title, description, due_date, status, category_id FROM tasks ORDER BY id";
        List<Task> tasks = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("FindAll failed", e);
        }
    }

    @Override
    public Optional<Task> update(Task task) {
        // Muss Optional.empty() liefern, wenn id nicht existiert (wie InMemory)
        if (!exists(task.getId())) return Optional.empty();

        String sql = """
            UPDATE tasks
               SET title=?,
                   description=?,
                   due_date=?,
                   status=?,
                   category_id=?
             WHERE id=?
            """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, toDbDate(task.getDueDate()));
            ps.setString(4, task.getStatus().name());
            if (task.getCategoryId() == null) ps.setNull(5, Types.BIGINT);
            else ps.setLong(5, task.getCategoryId());
            ps.setLong(6, task.getId());

            ps.executeUpdate();
            return Optional.of(task);

        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    @Override
    public Optional<Long> deleteById(long id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            return affected > 0 ? Optional.of(id) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }

    // -------- helpers --------

    private boolean exists(long id) {
        String sql = "SELECT 1 FROM tasks WHERE id=? LIMIT 1";
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exists check failed", e);
        }
    }

    private void bindTask(PreparedStatement ps, Task task) throws SQLException {
        ps.setLong(1, task.getId());
        ps.setString(2, task.getTitle());
        ps.setString(3, task.getDescription());
        ps.setString(4, toDbDate(task.getDueDate()));
        ps.setString(5, task.getStatus().name());
        if (task.getCategoryId() == null) ps.setNull(6, Types.BIGINT);
        else ps.setLong(6, task.getCategoryId());
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String description = rs.getString("description");

        String due = rs.getString("due_date");
        LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);

        String statusStr = rs.getString("status");
        TaskStatus status = (statusStr == null) ? TaskStatus.OPEN : TaskStatus.valueOf(statusStr);

        long cat = rs.getLong("category_id");
        Long categoryId = rs.wasNull() ? null : cat;

        return new Task(id, title, description, dueDate, status, categoryId);
    }

    private String toDbDate(LocalDate d) {
        return d == null ? null : d.toString();
    }
}
