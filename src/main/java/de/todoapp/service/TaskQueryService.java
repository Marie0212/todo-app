package de.todoapp.service;

import de.todoapp.domain.Task;
import java.util.List;

public interface TaskQueryService {
    List<Task> listTasks();
}
