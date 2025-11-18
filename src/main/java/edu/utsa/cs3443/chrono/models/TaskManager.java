package edu.utsa.cs3443.chrono.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages the master list of all Task objects.
 * This class handles adding, completing, and retrieving tasks,
 * using an ObservableList to allow for easy UI data binding.
 *
 * @author Collin Schiebel
 */
public class TaskManager {

    // An ObservableList will automatically notify UI listeners when tasks are
    // added, removed, or changed.
    private final ObservableList<Task> tasks;

    public TaskManager() {
        // Initialize with an empty, observable ArrayList.
        this.tasks = FXCollections.observableArrayList();
    }

    /**
     * Adds a new task to the master list.
     *
     * @param description The text description of the task.
     * @param pointValue  The points awarded for completing the task.
     */
    public void addTask(String description, int pointValue) {
        Task newTask = new Task(description, pointValue);
        tasks.add(newTask);
    }

    /**
     * Marks a specific task as complete.
     *
     * @param task The Task object to be completed.
     */
    public void completeTask(Task task) {
        if (task != null) {
            task.setIsComplete(true);
        }
    }

    /**
     * Removes a task from the list.
     * This is useful for when the user wants to delete a task.
     *
     * @param task The Task object to be removed.
     */
    public void removeTask(Task task) {
        if (task != null) {
            tasks.remove(task);
        }
    }

    /**
     * Gets the observable list of all tasks.
     * This list can be directly given to a UI component like a ListView.
     *
     * @return The ObservableList of tasks.
     */
    public ObservableList<Task> getTasks() {
        return tasks;
    }
}