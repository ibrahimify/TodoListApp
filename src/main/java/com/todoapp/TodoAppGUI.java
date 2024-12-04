package com.todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> sortComboBox;
    private JComboBox<String> filterComboBox;
    private HashMap<String, Category> categoryMap;

    public TodoAppGUI(HashMap<String, Category> categoryMap) {
        this.categoryMap = categoryMap;

        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            TaskManager.saveCategories(categoryMap);
            JOptionPane.showMessageDialog(this, "Tasks saved successfully.", "File Menu", JOptionPane.INFORMATION_MESSAGE);
        });
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "To-Do List Application\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Define table columns
        String[] columnNames = {"Title", "Description", "Due Date", "Completed", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        loadTasksIntoTable(getAllTasks());

        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons and dropdowns
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");

        sortComboBox = new JComboBox<>(new String[]{"Sort by Title", "Sort by Due Date", "Sort by Completed"});
        buttonPanel.add(sortComboBox);

        filterComboBox = new JComboBox<>(new String[]{"Show All", "Show Completed", "Show Pending"});
        buttonPanel.add(filterComboBox);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add Task
        addButton.addActionListener(e -> {
            AddTaskDialog dialog = new AddTaskDialog(this);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Task newTask = dialog.getTask();
                if (newTask != null) {
                    String category = newTask.getCategory();
                    categoryMap.computeIfAbsent(category, k -> new Category(category)).addTask(newTask);
                    refreshTable();
                }
            }
        });

        // Edit Task
        editButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentCategory = (String) tableModel.getValueAt(selectedRow, 4);
            if (!categoryMap.containsKey(currentCategory)) {
                JOptionPane.showMessageDialog(this, "The selected category does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = categoryMap.get(currentCategory);

            String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);

            Task taskToEdit = category.getTasks().stream()
                    .filter(task -> task.getTitle().equals(currentTitle))
                    .findFirst()
                    .orElse(null);

            if (taskToEdit == null) {
                JOptionPane.showMessageDialog(this, "Error locating the task in the selected category.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EditTaskDialog dialog = new EditTaskDialog(this,
                    taskToEdit.getTitle(),
                    taskToEdit.getDescription(),
                    taskToEdit.getDueDate().toString(),
                    taskToEdit.isCompleted(),
                    taskToEdit.getCategory()
            );
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Task updatedTask = dialog.getUpdatedTask();
                if (updatedTask != null) {
                    category.getTasks().remove(taskToEdit);
                    categoryMap.computeIfAbsent(updatedTask.getCategory(), k -> new Category(updatedTask.getCategory())).addTask(updatedTask);
                    refreshTable();
                }
            }
        });

        // Delete Task
        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentCategory = (String) tableModel.getValueAt(selectedRow, 4);
            if (!categoryMap.containsKey(currentCategory)) {
                JOptionPane.showMessageDialog(this, "The selected category does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = categoryMap.get(currentCategory);
            String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);

            Task taskToDelete = category.getTasks().stream()
                    .filter(task -> task.getTitle().equals(currentTitle))
                    .findFirst()
                    .orElse(null);

            if (taskToDelete != null) {
                category.getTasks().remove(taskToDelete);
                refreshTable();
            }
        });

        // Sorting and filtering
        sortComboBox.addActionListener(e -> refreshTable());
        filterComboBox.addActionListener(e -> refreshTable());

        setVisible(true);
    }

    private List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Category category : categoryMap.values()) {
            allTasks.addAll(category.getTasks());
        }
        return allTasks;
    }

    private void refreshTable() {
        List<Task> tasks = getAllTasks();

        // Apply filtering
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        if (selectedFilter != null) {
            switch (selectedFilter) {
                case "Show Completed":
                    tasks.removeIf(task -> !task.isCompleted());
                    break;
                case "Show Pending":
                    tasks.removeIf(Task::isCompleted);
                    break;
            }
        }

        // Apply sorting
        String selectedSort = (String) sortComboBox.getSelectedItem();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Sort by Title":
                    tasks.sort(Comparator.comparing(Task::getTitle));
                    break;
                case "Sort by Due Date":
                    tasks.sort(Comparator.comparing(Task::getDueDate));
                    break;
                case "Sort by Completed":
                    tasks.sort(Comparator.comparing(Task::isCompleted));
                    break;
            }
        }

        // Refresh table
        tableModel.setRowCount(0);
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
        HashMap<String, Category> categoryMap = TaskManager.loadCategories();
        if (categoryMap == null) {
            categoryMap = new HashMap<>();
        }
        new TodoAppGUI(categoryMap);
    }
}
