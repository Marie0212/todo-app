package de.todoapp.persistence;

import de.todoapp.domain.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCategoryRepository implements CategoryWriter, CategoryReader {

    private final Map<Long, Category> store = new ConcurrentHashMap<>();

    @Override
    public Category save(Category category) {
        store.put(category.getId(), category);
        return category;
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }
}
