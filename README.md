# To-Do List Application

## Overview

The **To-Do List Application** is a Java-based desktop task management system designed to help users organize and track their daily tasks efficiently. With a clean Swing-based graphical interface, robust functionality for task management, and data persistence through serialization, this project showcases the application of core Java concepts and design patterns.

---

## Features

- **Task Management**:
  - Add, edit, delete, and mark tasks as completed.
  - Categorize tasks and manage categories.
  - Set due dates and priorities for tasks.

- **Task Filtering and Sorting**:
  - Filter tasks based on completion status or category.
  - Sort tasks by title, due date, or priority.

- **Dark Mode**:
  - Switch between light and dark themes for improved user experience.

- **Data Persistence**:
  - Save and load tasks using Java serialization.

- **Statistics and Reporting** (Optional Enhancements):
  - Potential to expand with task statistics and progress tracking.

---

## Project Structure

```plaintext
TodoListApp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── todoapp/
│   │   │            ├── Category.java 
│   │   │            ├── Task.java 
│   │   │            ├── TaskManager.java 
│   │   │            ├── AddTaskDialog.java 
│   │   │            ├── EditTaskDialog.java 
│   │   │            ├── TodoAppGUI.java 
│   │   │            ├── Main.java 
│   │   ├── resources/
│   │       └── icons/
│   │           ├── add.png
│   │           ├── edit.png
│   │           ├── delete.png
│   │           ├── categories.png
│   │           ├── info.png
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── todoapp/
│   │   │            └── test/
│   │   │                ├── TaskTest.java
│   │   │                ├── SortingAndFilteringTest.java
│   │   │                ├── TaskManagerTest.java
├── pom.xml
└── .gitignore
```

## Setup Instructions

1. Verify Java installation:
   ```bash
   java -version
   ```
   Ensure it shows `Java 17` or higher.

2. Install Maven (if not already installed):
   ```bash
   mvn -version
   ```
   Ensure Maven is properly set up.

3. Clone the repository:
   ```bash
   git clone https://github.com/username/todolistapp.git
   ```

4. Navigate to the project directory:
   ```bash
   cd todolistapp
   ```

5. Build the project:
   ```bash
   mvn clean install
   ```

6. Run the application:
   ```bash
   java -cp target/classes com.todoapp.Main
   ```

---

## Documentation

### Class Diagram

![Class Diagram](docs/images/class-diagram.png)

### Sequence Diagrams

| Diagram Name        | Description                                   | Link                                                |
|---------------------|-----------------------------------------------|-----------------------------------------------------|
| Add Task            | Task creation flow.                          | ![Add Task](docs/diagrams/add-task-sequence.png)    |
| Edit Task           | Task modification flow.                      | ![Edit Task](docs/diagrams/edit-task-sequence.png)  |
| Delete Task         | Task deletion process.                       | ![Delete Task](docs/diagrams/delete-task-sequence.png) |
| Filter Tasks        | Filtering tasks based on criteria.           | ![Filter Tasks](docs/diagrams/filter-task-sequence.png) |
| Save and Load Tasks | Saving and loading tasks through persistence.| ![Save and Load](docs/diagrams/save-load-sequence.png) |

---

## User Interface Screenshots

### Light Mode
![Light Mode](docs/screenshots/light-mode.png)

### Dark Mode
![Dark Mode](docs/screenshots/dark-mode.png)

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/username/todolistapp.git
   ```
2. Navigate to the project directory:
   ```bash
   cd todolistapp
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -cp target/classes com.todoapp.Main
   ```

---

## Testing

Run the unit tests to validate the functionality:
```bash
mvn test
```

---

## Contributing

Contributions are welcome! Fork the repository and submit a pull request for review.
