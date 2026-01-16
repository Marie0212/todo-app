package de.todoapp.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final TaskStatus status;
    private final Long categoryId; // optional

    public Task(long id, String title, String description, LocalDate dueDate, TaskStatus status, Long categoryId) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        this.id = id;
        this.title = title.trim();
        this.description = (description == null || description.isBlank()) ? null : description.trim();
        this.dueDate = dueDate;
        this.status = Objects.requireNonNullElse(status, TaskStatus.OPEN);
        this.categoryId = categoryId;
    }

    // Backward compatible constructor (falls du noch alt aufrufst)
    public Task(long id, String title, String description, LocalDate dueDate, TaskStatus status) {
        this(id, title, description, dueDate, status, null);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public Long getCategoryId() { return categoryId; }

    public Task withStatus(TaskStatus newStatus) {
        return new Task(this.id, this.title, this.description, this.dueDate, newStatus, this.categoryId);
    }

    public Task withCategoryId(Long newCategoryId) {
        return new Task(this.id, this.title, this.description, this.dueDate, this.status, newCategoryId);
    }
}
