package de.todoapp.persistence;

import de.todoapp.domain.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTaskRepository implements TaskWriter {

    private final Map<Long, Task> store = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        store.put(task.getId(), task);
        return task;
    }
}
