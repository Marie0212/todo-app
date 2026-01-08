package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.TaskReader;
import de.todoapp.persistence.TaskWriter;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TaskService implements TaskCommandService, TaskQueryService {

    private final TaskWriter taskWriter;
    private final TaskReader taskReader;
    private final AtomicLong idSeq = new AtomicLong(0);

    public TaskService(TaskWriter taskWriter, TaskReader taskReader) {
        this.taskWriter = taskWriter;
        this.taskReader = taskReader;
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
}
