package edu.utsa.cs3443.chrono.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Manages the master list of all Task objects and handles data persistence.
 * Saves data to "data/tasks.csv".
 *
 * @author Collin Schiebel
 */
public class TaskManager {

    private final ObservableList<Task> tasks;

    // Define directory and file paths
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/tasks.csv";

    public TaskManager() {
        this.tasks = FXCollections.observableArrayList();
        loadTasks();
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void addTask(String description, int pointValue) {
        Task newTask = new Task(description, pointValue);
        tasks.add(newTask);
        saveTasks();
    }

    public void removeTask(Task task) {
        if (task != null) {
            tasks.remove(task);
            saveTasks();
        }
    }

    /**
     * Call this when a property (like isComplete or Notes) changes
     * to force a save to the file.
     */
    public void updateTask() {
        saveTasks();
    }

    /**
     * Saves the current list of tasks to 'data/tasks.csv'.
     * Creates the directory and file if they do not exist.
     */
    private void saveTasks() {
        try {
            // 1. Get the path object
            Path path = Paths.get(DATA_FILE);

            // 2. Ensure the parent directory ("data") exists
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            // 3. Write the file
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Task task : tasks) {
                    // Format: Description,Points,Complete,Date,Notes
                    String line = String.format("%s,%d,%b,%s,%s",
                            task.getDescription().replace(",", ";"), // Sanitize commas
                            task.pointValueProperty().get(),
                            task.isComplete(),
                            task.getDueDate().toString(),
                            task.notesProperty().get().replace(",", ";").replace("\n", "  ") // Sanitize newlines
                    );
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from 'data/tasks.csv'.
     * If the file or folder doesn't exist, it starts empty
     */
    private void loadTasks() {
        Path path = Paths.get(DATA_FILE);

        // If file doesn't exist, just return.
        // The "data" folder will be created the first time we save a task.
        if (Files.notExists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    // Basic fields
                    Task t = new Task(parts[0].replace(";", ","), Integer.parseInt(parts[1]));
                    t.setIsComplete(Boolean.parseBoolean(parts[2]));

                    // Optional fields (Date)
                    if (parts.length > 3 && !parts[3].equals("null")) {
                        try {
                            t.dueDateProperty().set(LocalDate.parse(parts[3]));
                        } catch (Exception e) {
                            // If date is malformed, stick to default
                        }
                    }

                    // Optional fields (Notes)
                    if (parts.length > 4) {
                        t.notesProperty().set(parts[4].replace(";", ",").replace("  ", "\n"));
                    }

                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}