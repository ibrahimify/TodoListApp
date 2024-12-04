package com.todoapp;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class AddTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField dueDateField;
    private JComboBox<String> categoryComboBox; // Dropdown for categories
    private boolean isConfirmed = false;

    public AddTaskDialog(Frame parent, HashMap<String, Category> categoryMap) {
        super(parent, "Add Task", true);
        setLayout(new GridLayout(5, 2));

        // Input fields
        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Due Date (YYYY-MM-DD):"));
        dueDateField = new JTextField();
        add(dueDateField);

        add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(categoryMap.keySet().toArray(new String[0])); // Dynamically load categories
        add(categoryComboBox);

        // Buttons
        JButton confirmButton = new JButton("Add");
        confirmButton.addActionListener(e -> {
            isConfirmed = true;
            dispose();
        });
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Task getTask() {
        try {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            LocalDate dueDate = LocalDate.parse(dueDateField.getText().trim());
            String category = (String) categoryComboBox.getSelectedItem();
            if (title.isEmpty() || category == null) {
                JOptionPane.showMessageDialog(this, "Title and Category are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return new Task(title, description, dueDate, false, category);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
