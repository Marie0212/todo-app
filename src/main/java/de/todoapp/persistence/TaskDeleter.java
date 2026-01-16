package de.todoapp.persistence;

import java.util.Optional;

public interface TaskDeleter {
    Optional<Long> deleteById(long id);
}
