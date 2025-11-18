package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Task;
import edu.utsa.cs3443.chrono.models.TaskManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controller for the Task Page.
 * Handles user input for adding new tasks and marking existing tasks as complete.
 *
 * @author Collin Schiebel
 */
public class TaskPageController {

    private TaskManager taskManager;

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private TextField newTaskField;

    @FXML
    private Button addTaskButton;

    /**
     * Initializes the controller class.
     * This method is automatically called after the fxml file has been loaded.
     * It sets up the model and binds the UI components to the model's data.
     */
    @FXML
    private void initialize() {
        this.taskManager = new TaskManager();
        taskListView.setItems(taskManager.getTasks());
    }

    /**
     * Handles the event when the "Add Task" button is clicked.
     * It gets the text from the new task input field and adds it as a new
     * task in the TaskManager.
     */
    @FXML
    private void handleAddTask() {
        String taskDescription = newTaskField.getText();
        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            // Defaulting to 10 points for this example
            taskManager.addTask(taskDescription, 10);
            newTaskField.clear();
        }
    }

    /**
     * Handles completing the currently selected task.
     * This would be connected to a "Complete" button or a checkbox.
     */
    @FXML
    private void handleCompleteTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskManager.completeTask(selectedTask);
        }
    }

    /**
     * Handles removing the currently selected task.
     * This would be connected to a "Remove" or "Delete" button.
     */
    @FXML
    private void handleRemoveTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskManager.removeTask(selectedTask);
        }
    }
}