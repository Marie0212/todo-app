package de.todoapp;

import de.todoapp.persistence.InMemoryCategoryRepository;
import de.todoapp.persistence.InMemoryTaskRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.CategoryService;
import de.todoapp.service.TaskService;

public class Main {

    public static void main(String[] args) {

        // Repos
        InMemoryTaskRepository taskRepo = new InMemoryTaskRepository();
        InMemoryCategoryRepository categoryRepo = new InMemoryCategoryRepository();

        // Services
        TaskService taskService = new TaskService(taskRepo, taskRepo, taskRepo, taskRepo);
        CategoryService categoryService = new CategoryService(categoryRepo, categoryRepo);

        // App (genau 4 Parameter, wie der Compiler es verlangt)
        ConsoleApp app = new ConsoleApp(
                taskService,      // TaskCommandService
                taskService,      // TaskQueryService
                categoryService,  // CategoryCommandService
                categoryService   // CategoryQueryService
        );

        app.run();
    }
}
