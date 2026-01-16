package de.todoapp;

import de.todoapp.persistence.InMemoryCategoryRepository;
import de.todoapp.persistence.InMemoryTaskRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.CategoryService;
import de.todoapp.service.TaskService;

public class Main {
    public static void main(String[] args) {
        var taskRepo = new InMemoryTaskRepository();
        var taskService = new TaskService(taskRepo, taskRepo, taskRepo, taskRepo);

        var categoryRepo = new InMemoryCategoryRepository();
        var categoryService = new CategoryService(categoryRepo, categoryRepo);

        var app = new ConsoleApp(taskService, taskService, categoryService, categoryService);
        app.run();
    }
}
