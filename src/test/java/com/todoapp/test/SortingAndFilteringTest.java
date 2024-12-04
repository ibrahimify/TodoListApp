package com.todoapp.test;

import com.todoapp.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortingAndFilteringTest {

    @Test
    public void testSortingByTitle() {
        // Arrange
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("B Task", "Description", LocalDate.of(2024, 12, 20), false, "Work"));
        tasks.add(new Task("A Task", "Description", LocalDate.of(2024, 12, 15), false, "Work"));

        // Act
        tasks.sort(Comparator.comparing(Task::getTitle));

        // Assert
        assertEquals("A Task", tasks.get(0).getTitle());
        assertEquals("B Task", tasks.get(1).getTitle());
    }

    @Test
    public void testFilteringByCompletionStatus() {
        // Arrange
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task 1", "Description", LocalDate.of(2024, 12, 20), true, "Work"));
        tasks.add(new Task("Task 2", "Description", LocalDate.of(2024, 12, 15), false, "Work"));

        // Act
        List<Task> completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .toList();

        // Assert
        assertEquals(1, completedTasks.size());
        assertEquals("Task 1", completedTasks.get(0).getTitle());
    }
}
