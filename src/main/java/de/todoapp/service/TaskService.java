package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.TaskDeleter;
import de.todoapp.persistence.TaskReader;
import de.todoapp.persistence.TaskUpdater;
import de.todoapp.persistence.TaskWriter;

import java.time.LocalDate;
import java.util.List;

public class TaskService implements TaskCommandService, TaskQueryService {

    private final TaskWriter writer;
    private final TaskReader reader;
    private final TaskUpdater updater;
    private final TaskDeleter deleter;

    public TaskService(TaskWriter writer, TaskReader reader, TaskUpdater updater, TaskDeleter deleter) {
        this.writer = writer;
        this.reader = reader;
        this.updater = updater;
        this.deleter = deleter;
    }

    @Override
    public Task addTask(String title, String description, LocalDate dueDate) {
        long nextId = reader.findAll().stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0L) + 1;

        // US-09 Task hat (id, title, description, dueDate, status, category)
        Task task = new Task(nextId, title, description, dueDate, TaskStatus.OPEN, null);
        return writer.save(task);
    }

    @Override
    public void markDone(long id) {
        Task existing = reader.findAll().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        updater.update(existing.withStatus(TaskStatus.DONE))
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    @Override
    public void deleteTask(long id) {
        deleter.deleteById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    @Override
    public List<Task> listTasks() {
        return reader.findAll();
    }

    @Override
    public List<Task> listTasksFiltered(TaskStatus status, String category) {
        return reader.findAll().stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> category == null || category.isBlank()
                        || (t.getCategory() != null && t.getCategory().equalsIgnoreCase(category.trim())))
                .toList();
    }
}