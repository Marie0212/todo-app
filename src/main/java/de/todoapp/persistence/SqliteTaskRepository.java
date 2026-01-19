package de.todoapp.persistence;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

public class SqliteTaskRepository implements TaskWriter, TaskReader, TaskUpdater {

    private final SqliteDatabase db;

    public SqliteTaskRepository(SqliteDatabase db) {
        this.db = db;
    }

    @Override
    public Task save(Task task) {
        try (Connection con = db.openConnection()) {
            DSLContext dsl = DSL.using(con, SQLDialect.SQLITE);
            dsl.insertInto(table("tasks"))
               .values(task.getId(),
                       task.getTitle(),
                       task.getDescription(),
                       task.getDueDate() == null ? null : task.getDueDate().toString(),
                       task.getStatus().name())
               .execute();
            return task;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Task> findAll() {
        try (Connection con = db.openConnection()) {
            DSLContext dsl = DSL.using(con, SQLDialect.SQLITE);
            return dsl.select().from("tasks").orderBy(field("id").asc()).fetch(this::mapTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Task> update(Task task) {
        try (Connection con = db.openConnection()) {
            DSLContext dsl = DSL.using(con, SQLDialect.SQLITE);
            int rows = dsl.update(table("tasks"))
                .set(field("title"), task.getTitle())
                .set(field("description"), task.getDescription())
                .set(field("due_date"), task.getDueDate() == null ? null : task.getDueDate().toString())
                .set(field("status"), task.getStatus().name())
                .where(field("id").eq(task.getId()))
                .execute();
            return rows == 1 ? Optional.of(task) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Task mapTask(Record r) {
        Long id = r.get("id", Long.class);
        String title = r.get("title", String.class);
        String description = r.get("description", String.class);
        String dueRaw = r.get("due_date", String.class);
        String statusRaw = r.get("status", String.class);

        LocalDate dueDate = (dueRaw == null || dueRaw.IsNullOrWhiteSpace()) ? null : LocalDate.parse(dueRaw);
        TaskStatus status = (statusRaw == null || statusRaw.IsNullOrWhiteSpace()) ? TaskStatus.OPEN : TaskStatus.valueOf(statusRaw);

        return new Task(id, title, description, dueDate, status);
    }
}
