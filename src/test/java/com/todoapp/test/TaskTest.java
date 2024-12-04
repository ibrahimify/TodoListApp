package com.todoapp.test;

import com.todoapp.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        // Arrange
        String title = "Complete Assignment";
        String description = "Finish the Java assignment";
        LocalDate dueDate = LocalDate.of(2024, 12, 10);
        String category = "Work";

        // Act
        Task task = new Task(title, description, dueDate, false, category);

        // Assert
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(dueDate, task.getDueDate());
        assertFalse(task.isCompleted());
        assertEquals(category, task.getCategory());
    }

    @Test
    public void testTaskEditing() {
        // Arrange
        Task task = new Task("Old Title", "Old Description", LocalDate.of(2024, 12, 5), false, "Personal");

        // Act
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setDueDate(LocalDate.of(2024, 12, 15));
        task.setCompleted(true);
        task.setCategory("Work");

        // Assert
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals(LocalDate.of(2024, 12, 15), task.getDueDate());
        assertTrue(task.isCompleted());
        assertEquals("Work", task.getCategory());
    }
}
