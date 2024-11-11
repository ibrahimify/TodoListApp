package com.todoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField dueDateField;
    private boolean isConfirmed = false;

    public AddTaskDialog(Frame parent) {
        super(parent, "Add Task", true);
        setLayout(new GridLayout(4, 2));

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

        // Buttons
        JButton confirmButton = new JButton("Add");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isConfirmed = true;
                dispose();
            }
        });
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Task getTask() {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            LocalDate dueDate = LocalDate.parse(dueDateField.getText());
            return new Task(title, description, dueDate);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
