package de.todoapp.service;

import de.todoapp.domain.Category;

public interface CategoryCommandService {
    Category addCategory(String name);
}
