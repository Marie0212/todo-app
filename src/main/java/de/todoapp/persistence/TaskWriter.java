package de.todoapp.persistence;

import de.todoapp.domain.Task;

public interface TaskWriter {
    Task save(Task task);
}
