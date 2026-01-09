package de.todoapp.persistence;

import de.todoapp.domain.Task;
import java.util.Optional;

public interface TaskUpdater {
    Optional<Task> update(Task task);
}
