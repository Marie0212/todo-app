package de.todoapp.domain;

public class Category {
    private final long id;
    private final String name;

    public Category(long id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("category name must not be empty");
        }
        this.id = id;
        this.name = name.trim();
    }

    public long getId() { return id; }
    public String getName() { return name; }
}
