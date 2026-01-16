package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.TaskDeleter;
import de.todoapp.persistence.TaskReader;
import de.todoapp.persistence.TaskUpdater;
import de.todoapp.persistence.TaskWriter;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

public class TaskService implements TaskCommandService, TaskQueryService {

    private final TaskWriter taskWriter;
    private final TaskReader taskReader;
    private final TaskUpdater taskUpdater;
    private final TaskDeleter taskDeleter;

    private final AtomicLong idSeq = new AtomicLong(0);

    public TaskService(TaskWriter taskWriter, TaskReader taskReader, TaskUpdater taskUpdater, TaskDeleter taskDeleter) {
        this.taskWriter = taskWriter;
        this.taskReader = taskReader;
        this.taskUpdater = taskUpdater;
        this.taskDeleter = taskDeleter;
    }

    @Override
    public Task addTask(String title, String description, LocalDate dueDate) {
        long id = idSeq.incrementAndGet();
        Task task = new Task(id, title, description, dueDate, TaskStatus.OPEN);
        return taskWriter.save(task);
    }

    @Override
    public List<Task> listTasks() {
        return taskReader.findAll();
    }

    @Override
    public void markDone(long id) {
        var existing = taskReader.findAll().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));

        var updated = existing.withStatus(TaskStatus.DONE);

        taskUpdater.update(updated)
                .orElseThrow(() -> new IllegalStateException("Could not update task: " + id));
    }

    @Override
    public void deleteTask(long id) {
        taskDeleter.deleteById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
    }
}
