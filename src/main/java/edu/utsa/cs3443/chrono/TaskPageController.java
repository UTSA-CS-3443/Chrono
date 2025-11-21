package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Task;
import edu.utsa.cs3443.chrono.models.TaskManager;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * Controller for the Checklist Page.
 * Handles user input for adding new tasks and marking existing tasks as complete.
 *
 * @author Collin Schiebel
 */
public class TaskPageController {

    private TaskManager taskManager;

    @FXML private ListView<Task> taskListView;
    @FXML private TextField newTaskField;
    @FXML private Button addTaskButton;
    @FXML private TextField detailDateField;
    @FXML private TextField detailTimeField;
    @FXML private TextField detailAutoCompleteField;
    @FXML private TextArea detailNotesArea;

    private Task currentSelectedTask;

    @FXML
    private void initialize() {
        this.taskManager = new TaskManager();
        taskListView.setItems(taskManager.getTasks());

        // --- NEW CHANGE: Allow pressing "Enter" to add a task ---
        newTaskField.setOnAction(event -> handleAddTask());

        taskListView.setCellFactory(param -> new TaskCardCell());
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadTaskDetails(newVal));
        setupEditListeners();
    }

    @FXML
    private void handleAddTask() {
        String taskDescription = newTaskField.getText();
        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            // Default points 10
            taskManager.addTask(taskDescription, 10);
            newTaskField.clear();
        }
    }

    private void loadTaskDetails(Task task) {
        currentSelectedTask = task;
        if (task != null) {
            detailDateField.setText(task.getDueDate().toString());
            detailTimeField.setText(task.timeEstimateProperty().get());
            detailAutoCompleteField.setText(task.autoCompleteTimeProperty().get());
            detailNotesArea.setText(task.notesProperty().get());

            detailDateField.setDisable(false);
            detailNotesArea.setDisable(false);
        } else {
            detailDateField.setText("");
            detailNotesArea.setText("");
            detailDateField.setDisable(true);
            detailNotesArea.setDisable(true);
        }
    }

    private void setupEditListeners() {
        // Save notes when user types
        detailNotesArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (currentSelectedTask != null) {
                currentSelectedTask.notesProperty().set(newVal);
                taskManager.updateTask();
            }
        });

        // Save Date when user clicks away
        detailDateField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && currentSelectedTask != null) {
                try {
                    LocalDate date = LocalDate.parse(detailDateField.getText());
                    currentSelectedTask.dueDateProperty().set(date);
                    taskManager.updateTask();
                    taskListView.refresh();
                } catch (DateTimeParseException e) {
                    // Ignore invalid dates
                }
            }
        });
    }

    /**
     * Custom Cell Factory
     */
    private class TaskCardCell extends ListCell<Task> {
        private final HBox rootBox;
        private final CheckBox checkBox;
        private final Label titleLabel;
        private final Label dateLabel;
        private final VBox textBox;

        // Track previous property to prevent binding leaks
        private BooleanProperty prevProperty;

        public TaskCardCell() {
            rootBox = new HBox(15);
            rootBox.setAlignment(Pos.CENTER_LEFT);
            rootBox.setPadding(new Insets(15));
            rootBox.getStyleClass().add("task-card");

            checkBox = new CheckBox();
            checkBox.getStyleClass().add("task-checkbox");

            titleLabel = new Label();
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

            dateLabel = new Label();
            dateLabel.setFont(Font.font("System", 12));
            dateLabel.setTextFill(Color.web("#555555"));

            textBox = new VBox(5, titleLabel, dateLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            rootBox.getChildren().addAll(checkBox, textBox);
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            // Unbind old task to prevent "checking all boxes" bug
            if (prevProperty != null) {
                checkBox.selectedProperty().unbindBidirectional(prevProperty);
                prevProperty = null;
            }

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                titleLabel.setText(task.getDescription());
                dateLabel.setText("Due: " + task.getDueDate().toString());

                prevProperty = task.isCompleteProperty();

                checkBox.setSelected(task.isComplete());
                checkBox.selectedProperty().bindBidirectional(prevProperty);

                checkBox.setOnAction(e -> taskManager.updateTask());

                updateStrikeThrough(task.isComplete());
                checkBox.selectedProperty().addListener((obs, old, isDone) -> updateStrikeThrough(isDone));

                setGraphic(rootBox);
                setStyle("-fx-background-color: transparent; -fx-padding: 5 0 5 0;");
            }
        }

        private void updateStrikeThrough(boolean isComplete) {
            if (isComplete) {
                titleLabel.setStyle("-fx-strikethrough: true;");
                rootBox.setOpacity(0.6);
            } else {
                titleLabel.setStyle("-fx-strikethrough: false;");
                rootBox.setOpacity(1.0);
            }
        }
    }
}