package de.todoapp.persistence;

import de.todoapp.domain.Category;

public interface CategoryWriter {
    Category save(Category category);
}
