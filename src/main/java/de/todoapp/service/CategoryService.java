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
    }

    @Override
    public Category addCategory(String name) {
        long id = idSeq.incrementAndGet();
        var category = new Category(id, name);
        return writer.save(category);
    }

    @Override
    public List<Category> listCategories() {
        return reader.findAll();
    }
}
