package com.todoapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category containing a list of tasks.
 * Implements Serializable to allow saving and loading.
 */
public class Category implements Serializable {
    private String name;
    private List<Task> tasks;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    // Getters and setters

    // Getter for the category name
    public String getName() {
        return name;
    }

    // Setter for the category name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the list of tasks
    public List<Task> getTasks() {
        return tasks;
    }

    //Adds a task to the category.
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks.size() +
                '}';
    }
}