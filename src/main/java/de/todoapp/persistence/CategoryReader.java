package de.todoapp.persistence;

import de.todoapp.domain.Category;

import java.util.List;

public interface CategoryReader {
    List<Category> findAll();
}
