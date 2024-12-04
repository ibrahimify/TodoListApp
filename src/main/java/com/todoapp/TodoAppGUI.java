package com.todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private HashMap<String, Category> categoryMap;
    private boolean isDarkMode = false;

    public TodoAppGUI(HashMap<String, Category> categoryMap) {
        this.categoryMap = categoryMap;

        // Set up the main frame
        setTitle("To-Do List Application");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem addTaskItem = new JMenuItem("Add Task", loadIcon("add.png"));
        JMenuItem editTaskItem = new JMenuItem("Edit Task", loadIcon("edit.png"));
        JMenuItem deleteTaskItem = new JMenuItem("Delete Task", loadIcon("delete.png"));
        JMenuItem manageCategoriesItem = new JMenuItem("Manage Categories", loadIcon("categories.png"));
        fileMenu.add(addTaskItem);
        fileMenu.add(editTaskItem);
        fileMenu.add(deleteTaskItem);
        fileMenu.add(manageCategoriesItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About", loadIcon("info.png"));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Task Table
        String[] columnNames = {"Title", "Description", "Due Date", "Category", "Completed"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class; // "Completed" column uses checkboxes
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only "Completed" column is editable
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 4) { // Completion status changed
                int row = e.getFirstRow();
                String title = (String) tableModel.getValueAt(row, 0);
                String category = (String) tableModel.getValueAt(row, 3);
                boolean completed = (Boolean) tableModel.getValueAt(row, 4);

                Task task = categoryMap.get(category).getTasks().stream()
                        .filter(t -> t.getTitle().equals(title))
                        .findFirst()
                        .orElse(null);
                if (task != null) {
                    task.setCompleted(completed);
                    TaskManager.saveCategories(categoryMap); // Save changes
                }
            }

        });

        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        add(tableScrollPane, BorderLayout.CENTER);

        // Categories Panel
        JPanel categoryPanel = createCategoryPanel();
        add(categoryPanel, BorderLayout.WEST);

        // Bottom Panel with Filter and Dark Mode Toggle
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filterPanel = new JPanel();
        filterComboBox = new JComboBox<>(new String[]{"Show All", "Show Completed", "Show Pending"});
        filterComboBox.addActionListener(e -> refreshTable(getFilteredTasks()));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JCheckBox darkModeToggle = new JCheckBox("Dark Mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode());

        bottomPanel.add(filterPanel, BorderLayout.WEST);
        bottomPanel.add(darkModeToggle, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Menu Actions
        addTaskItem.addActionListener(e -> addTask());
        editTaskItem.addActionListener(e -> editTask());
        deleteTaskItem.addActionListener(e -> deleteTask());
        manageCategoriesItem.addActionListener(e -> manageCategories());
        aboutItem.addActionListener(e -> showAboutDialog());

        refreshTable(getAllTasks());
        setVisible(true);
    }

    private ImageIcon loadIcon(String iconName) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconName));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconName);
            return null;
        }
    }

    private JPanel createCategoryPanel() {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));


        JLabel headerLabel = new JLabel("CATEGORIES");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryPanel.add(headerLabel, BorderLayout.NORTH);

        JList<String> categoryList = new JList<>(categoryMap.keySet().toArray(new String[0]));
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryList.addListSelectionListener(e -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                refreshTable(categoryMap.get(selectedCategory).getTasks());
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoryList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        categoryPanel.add(scrollPane, BorderLayout.CENTER);

        return categoryPanel;
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;

        Color backgroundColor = isDarkMode ? new Color(55, 55, 55) : Color.WHITE;
        Color foregroundColor = isDarkMode ? new Color(201, 209, 217) : Color.BLACK;
        Color headerColor = isDarkMode ? new Color(138, 145, 154) : Color.LIGHT_GRAY;

        getContentPane().setBackground(backgroundColor);
        applyDarkModeToComponents(this.getContentPane(), backgroundColor, foregroundColor);

        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            menuBar.setBackground(headerColor);
            menuBar.setForeground(foregroundColor);
        }

        SwingUtilities.updateComponentTreeUI(this);
    }


    private void applyDarkModeToComponents(Container container, Color backgroundColor, Color foregroundColor) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel || component instanceof JScrollPane) {
                component.setBackground(backgroundColor);
            }
            if (component instanceof JLabel || component instanceof JButton || component instanceof JComboBox) {
                component.setForeground(foregroundColor);
                component.setBackground(backgroundColor);
            }
            if (component instanceof JTable) {
                JTable table = (JTable) component;
                table.setBackground(backgroundColor);
                table.setForeground(foregroundColor);
                table.getTableHeader().setBackground(isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                table.getTableHeader().setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.BLACK);
            }
            if (component instanceof JList) {
                JList<?> list = (JList<?>) component;
                list.setBackground(backgroundColor);
                list.setForeground(foregroundColor);
            }
            if (component instanceof Container) {
                applyDarkModeToComponents((Container) component, backgroundColor, foregroundColor);
            }
        }
    }

    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setSize(300, 200);
        aboutDialog.setLayout(new BorderLayout(15, 15));
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel aboutLabel = new JLabel(
                "<html><div style='text-align: center;'>To-Do List Application<br>Version 1.0<br>Created by [Your Name]</div></html>",
                SwingConstants.CENTER
        );
        aboutDialog.add(aboutLabel, BorderLayout.CENTER);

        aboutLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> aboutDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        aboutDialog.add(buttonPanel, BorderLayout.SOUTH);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setVisible(true);
    }


    private void addTask() {
        AddTaskDialog dialog = new AddTaskDialog(this, categoryMap);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Task newTask = dialog.getTask();
            if (newTask != null) {
                String category = newTask.getCategory();
                categoryMap.computeIfAbsent(category, k -> new Category(category)).addTask(newTask);
                TaskManager.saveCategories(categoryMap); // Save immediately
                refreshTable(getAllTasks());
                refreshCategoryPanel(); // Update categories
            }
        }
    }


    private void editTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String title = (String) tableModel.getValueAt(selectedRow, 0);
        String description = (String) tableModel.getValueAt(selectedRow, 1);
        String dueDate = tableModel.getValueAt(selectedRow, 2).toString();
        String category = (String) tableModel.getValueAt(selectedRow, 3);
        boolean completed = (Boolean) tableModel.getValueAt(selectedRow, 4);

        EditTaskDialog dialog = new EditTaskDialog(this, title, description, dueDate, completed, category, categoryMap);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Task updatedTask = dialog.getUpdatedTask();
            if (updatedTask != null) {
                categoryMap.get(category).getTasks().removeIf(task -> task.getTitle().equals(title));
                categoryMap.computeIfAbsent(updatedTask.getCategory(), k -> new Category(updatedTask.getCategory())).addTask(updatedTask);

                refreshCategoryPanel();
                refreshTable(getAllTasks());
                TaskManager.saveCategories(categoryMap); // Save immediately
            }
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String title = (String) tableModel.getValueAt(selectedRow, 0);
        String category = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            categoryMap.get(category).getTasks().removeIf(task -> task.getTitle().equals(title));
            refreshTable(getAllTasks());
            TaskManager.saveCategories(categoryMap); // Save immediately
        }
    }

    private void manageCategories() {
        String[] options = {"Create New Category", "Delete Category", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "What do you want to do?",
                "Manage Categories", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) { // Create New Category
            String newCategory = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                if (!categoryMap.containsKey(newCategory)) {
                    categoryMap.put(newCategory, new Category(newCategory));
                    TaskManager.saveCategories(categoryMap); // Save immediately
                    refreshCategoryPanel(); // Update categories
                } else {
                    JOptionPane.showMessageDialog(this, "Category already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (choice == 1) { // Delete Category
            String[] categoryNames = categoryMap.keySet().toArray(new String[0]);
            String categoryToDelete = (String) JOptionPane.showInputDialog(this,
                    "Select category to delete:", "Delete Category",
                    JOptionPane.QUESTION_MESSAGE, null, categoryNames, categoryNames[0]);

            if (categoryToDelete != null) {
                categoryMap.remove(categoryToDelete);
                TaskManager.saveCategories(categoryMap); // Save immediately
                refreshCategoryPanel(); // Update categories
                refreshTable(getAllTasks());
            }
        }
    }


    private void refreshCategoryPanel() {
        Container contentPane = getContentPane();
        Component[] components = contentPane.getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBorder() != null && "CATEGORIES".equals(((JLabel) panel.getComponent(0)).getText())) {
                    // Clear and update the category list
                    panel.removeAll();
                    JPanel updatedPanel = createCategoryPanel();
                    contentPane.remove(panel);
                    contentPane.add(updatedPanel, BorderLayout.WEST);
                    revalidate();
                    repaint();
                    break;
                }
            }
        }
    }



    private List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Category category : categoryMap.values()) {
            allTasks.addAll(category.getTasks());
        }
        return allTasks;
    }

    private List<Task> getFilteredTasks() {
        List<Task> tasks = getAllTasks();
        String filter = (String) filterComboBox.getSelectedItem();
        if ("Show Completed".equals(filter)) {
            tasks.removeIf(task -> !task.isCompleted());
        } else if ("Show Pending".equals(filter)) {
            tasks.removeIf(Task::isCompleted);
        }
        return tasks;
    }

    private void refreshTable(List<Task> tasks) {
        tableModel.setRowCount(0); // Clear table
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getCategory(),
                    task.isCompleted()
            });
        }
        taskTable.revalidate(); // Revalidate the table to apply changes
        taskTable.repaint(); // Repaint the table to ensure it displays updated content
    }


    public static void main(String[] args) {
        HashMap<String, Category> categoryMap = TaskManager.loadCategories();
        if (categoryMap == null) {
            categoryMap = new HashMap<>();
        }
        new TodoAppGUI(categoryMap);
    }
}
