package de.todoapp.service;

import de.todoapp.domain.Task;

import java.time.LocalDate;

public interface TaskCommandService {
    Task addTask(String title, String description, LocalDate dueDate, String category);
    void markDone(long id);
    void deleteTask(long id);
}
