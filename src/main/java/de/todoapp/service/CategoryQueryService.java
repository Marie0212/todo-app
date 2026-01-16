package de.todoapp.service;

import de.todoapp.domain.Category;

import java.util.List;

public interface CategoryQueryService {
    List<Category> listCategories();
}
