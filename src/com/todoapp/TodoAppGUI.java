package com.todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private JTable taskTable; // Table to display tasks
    private DefaultTableModel tableModel; // Model to manage table data
    private JComboBox<String> sortComboBox; // Dropdown for sorting options
    private JComboBox<String> filterComboBox; // Dropdown for filtering options

    public TodoAppGUI(List<Task> tasks) {
        // Set up the main application window (frame)
        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Define the columns for the task table
        String[] columnNames = {"Title", "Description", "Due Date", "Completed", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        loadTasksIntoTable(tasks); // Load tasks into the table on startup

        // Wrap the table in a scroll pane for better usability
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons and dropdowns
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton saveButton = new JButton("Save Tasks");

        // Sorting options dropdown
        sortComboBox = new JComboBox<>(new String[]{"Sort by Title", "Sort by Due Date", "Sort by Completed"});
        buttonPanel.add(sortComboBox);

        // Filtering options dropdown
        filterComboBox = new JComboBox<>(new String[]{"Show All", "Show Completed", "Show Pending"});
        buttonPanel.add(filterComboBox);

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Task button logic
        addButton.addActionListener(e -> {
            AddTaskDialog dialog = new AddTaskDialog(TodoAppGUI.this);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Task newTask = dialog.getTask();
                if (newTask != null) {
                    tasks.add(newTask); // Add the task to the list
                    refreshTable(tasks); // Refresh the table to show the new task
                }
            }
        });

        // Edit Task button logic
        editButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch current task details from the table
            String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);
            String currentDescription = (String) tableModel.getValueAt(selectedRow, 1);
            String currentDueDate = tableModel.getValueAt(selectedRow, 2).toString();
            boolean currentCompleted = (boolean) tableModel.getValueAt(selectedRow, 3);
            String currentCategory = (String) tableModel.getValueAt(selectedRow, 4);

            // Open the Edit Task dialog
            EditTaskDialog dialog = new EditTaskDialog(this, currentTitle, currentDescription, currentDueDate, currentCompleted, currentCategory);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Task updatedTask = dialog.getUpdatedTask();
                if (updatedTask != null) {
                    // Update the task in the list and table
                    tasks.get(selectedRow).setTitle(updatedTask.getTitle());
                    tasks.get(selectedRow).setDescription(updatedTask.getDescription());
                    tasks.get(selectedRow).setDueDate(updatedTask.getDueDate());
                    tasks.get(selectedRow).setCompleted(updatedTask.isCompleted());
                    tasks.get(selectedRow).setCategory(updatedTask.getCategory());

                    tableModel.setValueAt(updatedTask.getTitle(), selectedRow, 0);
                    tableModel.setValueAt(updatedTask.getDescription(), selectedRow, 1);
                    tableModel.setValueAt(updatedTask.getDueDate(), selectedRow, 2);
                    tableModel.setValueAt(updatedTask.isCompleted(), selectedRow, 3);
                    tableModel.setValueAt(updatedTask.getCategory(), selectedRow, 4);
                }
            }
        });

        // Delete Task button logic
        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirm before deleting
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this task?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                tasks.remove(selectedRow); // Remove the task from the list
                tableModel.removeRow(selectedRow); // Remove the task from the table
            }
        });

        // Save Tasks button logic
        saveButton.addActionListener(e -> {
            TaskManager.saveTasks(tasks);
            JOptionPane.showMessageDialog(this, "Tasks saved successfully.", "Save Tasks", JOptionPane.INFORMATION_MESSAGE);
        });

        // Sort tasks when the sort dropdown changes
        sortComboBox.addActionListener(e -> refreshTable(tasks));

        // Filter tasks when the filter dropdown changes
        filterComboBox.addActionListener(e -> refreshTable(tasks));

        // Display the application window
        setVisible(true);
    }

    // Refresh the table with filtered and sorted tasks
    private void refreshTable(List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>();

        // Apply filtering
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        if (selectedFilter != null) {
            switch (selectedFilter) {
                case "Show All":
                    filteredTasks = new ArrayList<>(tasks);
                    break;
                case "Show Completed":
                    for (Task task : tasks) {
                        if (task.isCompleted()) {
                            filteredTasks.add(task);
                        }
                    }
                    break;
                case "Show Pending":
                    for (Task task : tasks) {
                        if (!task.isCompleted()) {
                            filteredTasks.add(task);
                        }
                    }
                    break;
            }
        }

        // Apply sorting
        String selectedSort = (String) sortComboBox.getSelectedItem();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Sort by Title":
                    filteredTasks.sort(Comparator.comparing(Task::getTitle));
                    break;
                case "Sort by Due Date":
                    filteredTasks.sort(Comparator.comparing(Task::getDueDate));
                    break;
                case "Sort by Completed":
                    filteredTasks.sort(Comparator.comparing(Task::isCompleted));
                    break;
            }
        }

        // Update the table with the filtered and sorted tasks
        tableModel.setRowCount(0); // Clear existing rows
        loadTasksIntoTable(filteredTasks);
    }

    // Load tasks into the table
    private void loadTasksIntoTable(List<Task> tasks) {
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.isCompleted(),
                    task.getCategory()
            });
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
