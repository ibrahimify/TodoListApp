package com.todoapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String name;
    private List<Task> tasks;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

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
