package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.TaskWriter;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class TaskService implements TaskCommandService {

    private final TaskWriter taskWriter;
    private final AtomicLong idSeq = new AtomicLong(0);

    public TaskService(TaskWriter taskWriter) {
        this.taskWriter = taskWriter;
    }

    @Override
    public Task addTask(String title, String description, LocalDate dueDate) {
        long id = idSeq.incrementAndGet();
        Task task = new Task(id, title, description, dueDate, TaskStatus.OPEN);
        return taskWriter.save(task);
    }
}
