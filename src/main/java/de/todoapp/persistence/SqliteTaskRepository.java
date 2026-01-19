package de.todoapp.persistence;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SqliteTaskRepository implements TaskWriter, TaskReader, TaskUpdater, TaskDeleter {

    private final SqliteDatabase db;

    public SqliteTaskRepository(SqliteDatabase db) {
        this.db = db;
    }

    @Override
    public Task save(Task task) {
        // TODO US-07: insert into SQLite
        return task;
    }

    @Override
    public List<Task> findAll() {
        // TODO US-07: select from SQLite
        return List.of();
    }

    @Override
    public Optional<Task> update(Task task) {
        // TODO US-07: update SQLite
        return Optional.of(task);
    }

    @Override
    public Optional<Long> deleteById(long id) {
        // TODO US-07: delete from SQLite
        // return Optional.of(id) wenn gelöscht, sonst Optional.empty()
        return Optional.empty();
    }

    static LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return LocalDate.parse(raw);
    }

    static TaskStatus parseStatus(String raw) {
        if (raw == null || raw.isBlank()) return TaskStatus.OPEN;
        return TaskStatus.valueOf(raw);
    }
}
