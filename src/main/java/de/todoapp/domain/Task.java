package de.todoapp.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final TaskStatus status;
    private final String category; // optional

    public Task(long id, String title, String description, LocalDate dueDate, TaskStatus status, String category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        this.id = id;
        this.title = title.trim();
        this.description = (description == null || description.isBlank()) ? null : description.trim();
        this.dueDate = dueDate;
        this.status = Objects.requireNonNullElse(status, TaskStatus.OPEN);
        this.category = (category == null || category.isBlank()) ? null : category.trim();
    }

    // Backward-compatible constructor (falls andere Stellen noch den alten Konstruktor nutzen)
    public Task(long id, String title, String description, LocalDate dueDate, TaskStatus status) {
        this(id, title, description, dueDate, status, null);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public String getCategory() { return category; }

    public Task withStatus(TaskStatus newStatus) {
        return new Task(this.id, this.title, this.description, this.dueDate, newStatus, this.category);
    }

    public Task withCategory(String newCategory) {
        return new Task(this.id, this.title, this.description, this.dueDate, this.status, newCategory);
    }
}
