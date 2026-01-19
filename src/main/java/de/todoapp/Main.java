package de.todoapp;

import de.todoapp.persistence.SqliteDatabase;
import de.todoapp.persistence.SqliteTaskRepository;
import de.todoapp.presentation.ConsoleApp;
import de.todoapp.service.TaskService;

public class Main {
    public static void main(String[] args) {
        var db = new SqliteDatabase("jdbc:sqlite:data/todo.db");
        db.initSchema();

        var repo = new SqliteTaskRepository(db);
        var service = new TaskService(repo, repo, repo);

        new ConsoleApp(service, service).run();
    }
}
