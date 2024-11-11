package com.todoapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create sample tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Complete Project", "Finish the Java project by next week", LocalDate.of(2024, 11, 15)));
        tasks.add(new Task("Buy Groceries", "Get vegetables and fruits", LocalDate.of(2024, 11, 10)));

        // Save tasks to file
        TaskManager.saveTasks(tasks);

        // Load tasks from file and print them
        List<Task> loadedTasks = TaskManager.loadTasks();
        if (loadedTasks != null) {
            for (Task task : loadedTasks) {
                System.out.println(task);
            }
        }
    }
}
