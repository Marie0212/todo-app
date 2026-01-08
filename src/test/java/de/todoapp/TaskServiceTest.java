package de.todoapp.service;

import de.todoapp.persistence.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceListTest {

    @Test
    void listTasks_returnsCreatedTasks() {
        var repo = new InMemoryTaskRepository();
        var service = new TaskService(repo, repo);

        service.addTask("A", null, null);
        service.addTask("B", null, null);

        var tasks = service.listTasks();
        assertEquals(2, tasks.size());
    }
}
