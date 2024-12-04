package com.todoapp.test;

import com.todoapp.Category;
import com.todoapp.Task;
import com.todoapp.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    @Test
    public void testSaveAndLoadCategories() {
        // Arrange
        HashMap<String, Category> categoryMap = new HashMap<>();
        Category work = new Category("Work");
        work.addTask(new Task("Work Task", "Description", LocalDate.of(2024, 12, 20), false, "Work"));
        categoryMap.put("Work", work);

        // Act
        TaskManager.saveCategories(categoryMap);
        HashMap<String, Category> loadedCategoryMap = TaskManager.loadCategories();

        // Assert
        assertNotNull(loadedCategoryMap);
        assertEquals(1, loadedCategoryMap.size());
        assertTrue(loadedCategoryMap.containsKey("Work"));
        assertEquals(1, loadedCategoryMap.get("Work").getTasks().size());
        assertEquals("Work Task", loadedCategoryMap.get("Work").getTasks().get(0).getTitle());
    }
}
