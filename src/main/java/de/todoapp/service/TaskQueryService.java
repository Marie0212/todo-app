package de.todoapp.service;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;

import java.util.List;

public interface TaskQueryService {
    List<Task> listTasks();

    // US-09
    List<Task> listTasksFiltered(TaskStatus status, String category);
}
