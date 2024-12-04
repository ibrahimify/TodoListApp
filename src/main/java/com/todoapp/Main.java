package com.todoapp;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        // Load categories from file or create an empty HashMap
        HashMap<String, Category> categoryMap = TaskManager.loadCategories();
        if (categoryMap == null) {
            categoryMap = new HashMap<>();
        }

        // Launch the GUI
        new TodoAppGUI(categoryMap);
    }
}
