package de.todoapp.persistence;

import de.todoapp.domain.Task;
import java.util.List;

public interface TaskReader {
    List<Task> findAll();
}
