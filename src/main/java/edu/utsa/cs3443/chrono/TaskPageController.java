package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.ShopModel;
import edu.utsa.cs3443.chrono.models.Task;
import edu.utsa.cs3443.chrono.models.TaskManager;
import javafx.beans.property.BooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Controller for the Checklist Page.
 * Handles user input for adding new tasks, modifying task details,
 * filtering completed tasks, and managing coin rewards upon completion.
 *
 * @author Collin Schiebel
 */
public class TaskPageController {

    private TaskManager taskManager;
    private ShopModel shopModel;
    private FilteredList<Task> filteredTasks;

    @FXML private ListView<Task> taskListView;
    @FXML private TextField newTaskField;
    @FXML private Button addTaskButton;
    @FXML private CheckBox showCompletedCheckBox;

    @FXML private TextField detailNameField;
    @FXML private TextField detailDateField;
    @FXML private TextField detailTimeField;
    @FXML private TextArea detailNotesArea;

    private Task currentSelectedTask;

    /**
     * Initializes the controller, sets up the task list filter, and configures event listeners.
     */
    @FXML
    private void initialize() {
        this.taskManager = new TaskManager();
        this.shopModel = new ShopModel();

        this.filteredTasks = new FilteredList<>(taskManager.getTasks(), task -> true);
        taskListView.setItems(filteredTasks);

        newTaskField.setOnAction(event -> handleAddTask());

        taskListView.setCellFactory(param -> new TaskCardCell());
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadTaskDetails(newVal));
        setupEditListeners();

        // Default to showing all tasks
        showCompletedCheckBox.setSelected(true);
    }

    /**
     * Adds a new task with the description provided in the text field.
     */
    @FXML
    private void handleAddTask() {
        String taskDescription = newTaskField.getText();
        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            taskManager.addTask(taskDescription, 10);
            newTaskField.clear();
        }
    }

    /**
     * Toggles the visibility of completed tasks in the list view.
     */
    @FXML
    private void handleFilterToggle() {
        boolean showCompleted = showCompletedCheckBox.isSelected();

        filteredTasks.setPredicate(task -> {
            if (showCompleted) {
                return true;
            } else {
                return !task.isComplete();
            }
        });
    }

    /**
     * Loads the details of the selected task into the right-hand sidebar.
     * @param task The task to display.
     */
    private void loadTaskDetails(Task task) {
        currentSelectedTask = task;
        if (task != null) {
            detailNameField.setText(task.getDescription());
            detailDateField.setText(task.getDueDate().toString());
            detailTimeField.setText(task.timeEstimateProperty().get());
            detailNotesArea.setText(task.notesProperty().get());

            detailNameField.setDisable(false);
            detailDateField.setDisable(false);
            detailTimeField.setDisable(false);
            detailNotesArea.setDisable(false);
        } else {
            detailNameField.setText("");
            detailDateField.setText("");
            detailTimeField.setText("");
            detailNotesArea.setText("");

            detailNameField.setDisable(true);
            detailDateField.setDisable(true);
            detailTimeField.setDisable(true);
            detailNotesArea.setDisable(true);
        }
    }

    /**
     * Sets up listeners to save changes to task details (Name, Date, Time, Notes)
     * when the user presses Enter or clicks away from the field.
     */
    private void setupEditListeners() {
        // --- Task Name ---
        detailNameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && currentSelectedTask != null) {
                currentSelectedTask.setDescription(detailNameField.getText());
                taskManager.updateTask();
                taskListView.refresh();
            }
        });
        detailNameField.setOnAction(event -> {
            if (currentSelectedTask != null) {
                currentSelectedTask.setDescription(detailNameField.getText());
                taskManager.updateTask();
                taskListView.refresh();
                detailNameField.getParent().requestFocus();
            }
        });

        // --- Due Date ---
        detailDateField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && currentSelectedTask != null) {
                saveDate();
            }
        });
        detailDateField.setOnAction(event -> {
            if (currentSelectedTask != null) {
                saveDate();
                detailDateField.getParent().requestFocus();
            }
        });

        // --- Time Estimate ---
        detailTimeField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && currentSelectedTask != null) {
                currentSelectedTask.timeEstimateProperty().set(detailTimeField.getText());
                taskManager.updateTask();
                taskListView.refresh();
            }
        });
        detailTimeField.setOnAction(event -> {
            if (currentSelectedTask != null) {
                currentSelectedTask.timeEstimateProperty().set(detailTimeField.getText());
                taskManager.updateTask();
                taskListView.refresh();
                detailTimeField.getParent().requestFocus();
            }
        });

        // --- Notes ---
        detailNotesArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && currentSelectedTask != null) {
                currentSelectedTask.notesProperty().set(detailNotesArea.getText());
                taskManager.updateTask();
            }
        });
    }

    private void saveDate() {
        try {
            LocalDate date = LocalDate.parse(detailDateField.getText());
            currentSelectedTask.dueDateProperty().set(date);
            taskManager.updateTask();
            taskListView.refresh();
        } catch (DateTimeParseException e) {
            if (currentSelectedTask != null) {
                detailDateField.setText(currentSelectedTask.getDueDate().toString());
            }
        }
    }

    /**
     * Custom ListCell implementation to display tasks with a CheckBox and Delete button.
     */
    private class TaskCardCell extends ListCell<Task> {
        private final HBox rootBox;
        private final CheckBox checkBox;
        private final Label titleLabel;
        private final Label dateLabel;
        private final VBox textBox;
        private final Button deleteButton;

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
            titleLabel.getStyleClass().add("task-title");

            dateLabel = new Label();
            dateLabel.setFont(Font.font("System", 12));
            dateLabel.getStyleClass().add("date-label");

            textBox = new VBox(5, titleLabel, dateLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 12px;");
            deleteButton.setPrefHeight(30);

            rootBox.getChildren().addAll(checkBox, textBox, deleteButton);

            selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    rootBox.getStyleClass().add("selected");
                    titleLabel.getStyleClass().add("selected");
                    dateLabel.getStyleClass().add("selected");
                } else {
                    rootBox.getStyleClass().remove("selected");
                    titleLabel.getStyleClass().remove("selected");
                    dateLabel.getStyleClass().remove("selected");
                }
            });
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

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

                deleteButton.setOnAction(e -> {
                    taskManager.removeTask(task);
                    if (currentSelectedTask == task) {
                        loadTaskDetails(null);
                    }
                });

                checkBox.setOnAction(e -> {
                    boolean isNowComplete = checkBox.isSelected();
                    taskManager.updateTask();

                    if (isNowComplete) {
                        shopModel.updateTotalCoins(20);
                    } else {
                        shopModel.updateTotalCoins(-20);
                    }

                    if (!showCompletedCheckBox.isSelected() && isNowComplete) {
                        handleFilterToggle();
                    }
                });

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