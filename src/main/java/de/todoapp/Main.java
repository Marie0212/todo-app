package de.todoapp;

import de.todoapp.persistence.InMemoryCategoryRepository;
import de.todoapp.persistence.SqliteDatabase;
import de.todoapp.persistence.SqliteTaskRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.CategoryService;
import de.todoapp.service.TaskService;

public class Main {
    public static void main(String[] args) {
        var db = new SqliteDatabase("jdbc:sqlite:data/todo.db");
        db.initSchema();

        var taskRepo = new SqliteTaskRepository(db);

        // Categories erstmal InMemory (US-07 betrifft Tasks/SQLite)
        var categoryRepo = new InMemoryCategoryRepository();

        var taskService = new TaskService(taskRepo, taskRepo, taskRepo, taskRepo);
        var categoryService = new CategoryService(categoryRepo, categoryRepo);

        new ConsoleApp(taskService, taskService, categoryService, categoryService).run();
    }
}
