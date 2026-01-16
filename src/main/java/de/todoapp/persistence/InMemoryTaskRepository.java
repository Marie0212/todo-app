package de.todoapp.persistence;

import de.todoapp.domain.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTaskRepository implements TaskWriter, TaskReader, TaskUpdater, TaskDeleter {

    private final Map<Long, Task> store = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Task> update(Task task) {
        if (!store.containsKey(task.getId())) {
            return Optional.empty();
        }
        store.put(task.getId(), task);
        return Optional.of(task);
    }

    @Override
    public Optional<Long> deleteById(long id) {
        return (store.remove(id) != null) ? Optional.of(id) : Optional.empty();
    }
}
