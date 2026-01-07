package de.todoapp;

import de.todoapp.persistence.InMemoryTaskRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.TaskService;

public class Main {
    public static void main(String[] args) {
        var repo = new InMemoryTaskRepository();
        var service = new TaskService(repo);
        var app = new ConsoleApp(service);
        app.run();
    }
}
