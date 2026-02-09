package de.todoapp;

import de.todoapp.persistence.InMemoryCategoryRepository;
import de.todoapp.persistence.SQLiteTaskRepository;
import de.todoapp.persistence.SQLiteCategoryRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.CategoryService;
import de.todoapp.service.TaskService;

public class Main {
    public static void main(String[] args) {

        String dbPath = new java.io.File("data/todo.db").getAbsolutePath();        String url = "jdbc:sqlite:" + dbPath;
        System.out.println("DB file = " + dbPath);

        var taskRepo = new SQLiteTaskRepository(url);
        var taskService = new TaskService(taskRepo, taskRepo, taskRepo, taskRepo);

        var categoryRepo = new SQLiteCategoryRepository(url);
        var categoryService = new CategoryService(categoryRepo, categoryRepo);


        var app = new ConsoleApp(taskService, taskService, categoryService, categoryService);
        app.run();
    }
}
