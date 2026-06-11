package de.todoapp;

import de.todoapp.persistence.CategoryReader;
import de.todoapp.persistence.CategoryWriter;
import de.todoapp.persistence.TaskDeleter;
import de.todoapp.persistence.TaskReader;
import de.todoapp.persistence.TaskUpdater;
import de.todoapp.persistence.TaskWriter;
import de.todoapp.service.CategoryService;
import de.todoapp.service.TaskService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TodoAppTest {

    @Test
    void categoryServiceHasExpectedConstructorDependencies() throws NoSuchMethodException {
        Constructor<CategoryService> constructor =
                CategoryService.class.getConstructor(CategoryWriter.class, CategoryReader.class);

        assertNotNull(constructor);
    }

    @Test
    void taskServiceHasExpectedConstructorDependencies() throws NoSuchMethodException {
        Constructor<TaskService> constructor =
                TaskService.class.getConstructor(
                        TaskWriter.class,
                        TaskReader.class,
                        TaskUpdater.class,
                        TaskDeleter.class
                );

        assertNotNull(constructor);
    }

    @Test
    void categoryServiceContainsRealApplicationMethods() {
        Method[] methods = CategoryService.class.getDeclaredMethods();

        assertTrue(methods.length > 0);
        assertTrue(Arrays.stream(methods).anyMatch(method -> !method.isSynthetic()));
    }

    @Test
    void taskServiceContainsRealApplicationMethods() {
        Method[] methods = TaskService.class.getDeclaredMethods();

        assertTrue(methods.length > 0);
        assertTrue(Arrays.stream(methods).anyMatch(method -> !method.isSynthetic()));
    }
}
