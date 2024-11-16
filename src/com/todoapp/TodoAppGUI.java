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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(TodoAppGUI.this, "Please select a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get current task details
                String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);
                String currentDescription = (String) tableModel.getValueAt(selectedRow, 1);
                String currentDueDate = tableModel.getValueAt(selectedRow, 2).toString();
                boolean currentCompleted = (boolean) tableModel.getValueAt(selectedRow, 3);

                // Show edit dialog
                EditTaskDialog dialog = new EditTaskDialog(TodoAppGUI.this, currentTitle, currentDescription, currentDueDate, currentCompleted);
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    Task updatedTask = dialog.getUpdatedTask();
                    if (updatedTask != null) {
                        // Update task list and table
                        tasks.get(selectedRow).setTitle(updatedTask.getTitle());
                        tasks.get(selectedRow).setDescription(updatedTask.getDescription());
                        tasks.get(selectedRow).setDueDate(updatedTask.getDueDate());
                        tasks.get(selectedRow).setCompleted(updatedTask.isCompleted());

                        tableModel.setValueAt(updatedTask.getTitle(), selectedRow, 0);
                        tableModel.setValueAt(updatedTask.getDescription(), selectedRow, 1);
                        tableModel.setValueAt(updatedTask.getDueDate(), selectedRow, 2);
                        tableModel.setValueAt(updatedTask.isCompleted(), selectedRow, 3);
                    }
                }
            }
        });

        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(TodoAppGUI.this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirm before deleting
                int confirm = JOptionPane.showConfirmDialog(
                        TodoAppGUI.this,
                        "Are you sure you want to delete this task?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Remove the task from the list and table
                    tasks.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        // Save button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save tasks to file
                TaskManager.saveTasks(tasks);
                JOptionPane.showMessageDialog(TodoAppGUI.this, "Tasks saved successfully.", "Save Tasks", JOptionPane.INFORMATION_MESSAGE);
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
