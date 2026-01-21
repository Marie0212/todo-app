package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.TaskDeleter;
import de.todoapp.persistence.TaskReader;
import de.todoapp.persistence.TaskUpdater;
import de.todoapp.persistence.TaskWriter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskService implements TaskCommandService, TaskQueryService {

    private final TaskWriter taskWriter;
    private final TaskReader taskReader;
    private final TaskUpdater taskUpdater;
    private final TaskDeleter taskDeleter;

    public TaskService(TaskWriter taskWriter, TaskReader taskReader, TaskUpdater taskUpdater, TaskDeleter taskDeleter) {
        this.taskWriter = taskWriter;
        this.taskReader = taskReader;
        this.taskUpdater = taskUpdater;
        this.taskDeleter = taskDeleter;
    }

    @Override
    public Task addTask(String title, String description, LocalDate dueDate) {
        // Wenn du irgendwo anders ID generierst, lass es so wie bei dir.
        // Hier als fallback: maxId+1
        long nextId = taskReader.findAll().stream().mapToLong(Task::getId).max().orElse(0L) + 1;
        Task task = new Task(nextId, title, description, dueDate, TaskStatus.OPEN, null);
        return taskWriter.save(task);
    }

    @Override
    public void markDone(long id) {
        Task existing = taskReader.findAll().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        taskUpdater.update(existing.withStatus(TaskStatus.DONE))
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    @Override
    public Optional<Long> deleteById(long id) {
        return taskDeleter.deleteById(id);
    }

    @Override
    public List<Task> listTasks() {
        return taskReader.findAll();
    }

    @Override
    public List<Task> listTasksFiltered(TaskStatus status, String category) {
        return taskReader.findAll().stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> category == null || category.isBlank() || (t.getCategory() != null && t.getCategory().equalsIgnoreCase(category.trim())))
                .toList();
    }
}
