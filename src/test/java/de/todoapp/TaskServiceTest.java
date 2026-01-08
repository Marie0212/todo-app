package de.todoapp.service;

import de.todoapp.persistence.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Test
    void addTask_requiresTitle() {
        TaskService service = new TaskService(new InMemoryTaskRepository());
        assertThrows(IllegalArgumentException.class, () -> service.addTask("   ", null, null));
    }

    @Test
    void addTask_createsTaskWithId() {
        TaskService service = new TaskService(new InMemoryTaskRepository());
        var task = service.addTask("Test", null, null);
        assertTrue(task.getId() > 0);
        assertEquals("Test", task.getTitle());
    }
}
