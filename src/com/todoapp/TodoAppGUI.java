package com.todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TodoAppGUI(List<Task> tasks) {
        // Set up the frame
        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the table model and table
        String[] columnNames = {"Title", "Description", "Due Date", "Completed"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        loadTasksIntoTable(tasks);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton saveButton = new JButton("Save Tasks");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTaskDialog dialog = new AddTaskDialog(TodoAppGUI.this);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    Task newTask = dialog.getTask();
                    if (newTask != null) {
                        tasks.add(newTask);
                        tableModel.addRow(new Object[]{newTask.getTitle(), newTask.getDescription(), newTask.getDueDate(), newTask.isCompleted()});
                    }
                }
            }
        });


        // Display the frame
        setVisible(true);
    }

    // Method to load tasks into the table
    private void loadTasksIntoTable(List<Task> tasks) {
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getDueDate(), task.isCompleted()});
        }
    }

    public static void main(String[] args) {
        // Load tasks from file or create an empty list
        List<Task> tasks = TaskManager.loadTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        // Launch the GUI
        new TodoAppGUI(tasks);
    }
}
