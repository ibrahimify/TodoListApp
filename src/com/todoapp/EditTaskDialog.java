package com.todoapp;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField dueDateField;
    private JCheckBox completedCheckBox;
    private boolean isConfirmed = false;

    public EditTaskDialog(Frame parent, String title, String description, String dueDate, boolean completed) {
        super(parent, "Edit Task", true);
        setLayout(new GridLayout(5, 2));

        // Input fields pre-filled with current task data
        add(new JLabel("Title:"));
        titleField = new JTextField(title);
        add(titleField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField(description);
        add(descriptionField);

        add(new JLabel("Due Date (YYYY-MM-DD):"));
        dueDateField = new JTextField(dueDate);
        add(dueDateField);

        add(new JLabel("Completed:"));
        completedCheckBox = new JCheckBox();
        completedCheckBox.setSelected(completed);
        add(completedCheckBox);

        // Buttons
        JButton confirmButton = new JButton("Update");
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

    public Task getUpdatedTask() {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            LocalDate dueDate = LocalDate.parse(dueDateField.getText());
            boolean completed = completedCheckBox.isSelected();
            return new Task(title, description, dueDate, completed);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
