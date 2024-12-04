package com.todoapp;

import java.io.*;
import java.util.HashMap;

public class TaskManager {
    private static final String FILE_NAME = "categories.ser";

    // Save categories to a file using serialization
    public static void saveCategories(HashMap<String, Category> categoryMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(categoryMap);
            System.out.println("Categories saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving categories: " + e.getMessage());
        }
    }

    // Load categories from a file using deserialization
    @SuppressWarnings("unchecked")
    public static HashMap<String, Category> loadCategories() {
        File file = new File(FILE_NAME);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("No existing categories file found. Starting fresh.");
            return null; // Return null to indicate no data is available
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, Category>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
        return null;
    }
}