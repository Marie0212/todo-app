package de.todoapp.service;

import de.todoapp.domain.Category;
import de.todoapp.persistence.CategoryReader;
import de.todoapp.persistence.CategoryWriter;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryService implements CategoryCommandService, CategoryQueryService {

    private final CategoryWriter writer;
    private final CategoryReader reader;
    private final AtomicLong idSeq = new AtomicLong(0);

    public CategoryService(CategoryWriter writer, CategoryReader reader) {
        this.writer = writer;
        this.reader = reader;

        long highestExistingId = reader.findAll().stream()
                .mapToLong(Category::getId)
                .max()
                .orElse(0L);

        idSeq.set(highestExistingId);
    }

    @Override
    public Category addCategory(String name) {
        String trimmedName = name == null ? "" : name.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Kategorie-Name darf nicht leer sein."
            );
        }

        for (Category existingCategory : reader.findAll()) {
            if (existingCategory.getName().equalsIgnoreCase(trimmedName)) {
                return existingCategory;
            }
        }

        long id = idSeq.incrementAndGet();
        Category category = new Category(id, trimmedName);
        return writer.save(category);
    }

    @Override
    public List<Category> listCategories() {
        return reader.findAll();
    }
}