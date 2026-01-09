package de.todoapp.service;

import de.todoapp.domain.TaskStatus;
import de.todoapp.persistence.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceDoneTest {

    @Test
    void markDone_setsStatusToDone() {
        var repo = new InMemoryTaskRepository();
        var service = new TaskService(repo, repo, repo);

        var t = service.addTask("Test", null, null);
        service.markDone(t.getId());

        var tasks = service.listTasks();
        var updated = tasks.stream().filter(x -> x.getId() == t.getId()).findFirst().orElseThrow();
        assertEquals(TaskStatus.DONE, updated.getStatus());
    }
}