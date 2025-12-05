package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.ShopModel;
import edu.utsa.cs3443.chrono.models.Task;
import edu.utsa.cs3443.chrono.models.TaskManager;
import edu.utsa.cs3443.chrono.models.TimerModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;


/**
 * controller class for operations on timer screen
 *
 * @author Davis Howe
 */
public class TimerController {

    @FXML
    private Button assignTaskButton;

    @FXML
    private Label timerText;

    @FXML
    private Label currentTaskLabel;

    @FXML
    private Button minusHoursButton;

    @FXML
    private Button minusMinutesButton;

    @FXML
    private Button minusSecondsButton;

    @FXML
    private Button plusHoursButton;

    @FXML
    private Button plusMinutesButton;

    @FXML
    private Button plusSecondsButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private ListView<Task> taskList;

    private TimerModel timer;
    private Timeline timeline;
    private Task selectedTask;
    private TaskManager tm;
    private ShopModel shop;

    @FXML
    public void initialize() {
        //create new timer object with default values of 0
        timer = new TimerModel(0, 0, 0);

        timerText.setText(timer.getCurrentTime());

        //new timeline and keyframe which calls every second until stopped
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        tm = new TaskManager();
        shop = new ShopModel();

        ObservableList<Task> tasks = FXCollections.observableArrayList();

        //only adds tasks that are not completed
        for(int i = 0; i < tm.getTasks().size();i++) {
            if (!tm.getTasks().get(i).isComplete()) {
                tasks.add(tm.getTasks().get(i));
            }
        }

        taskList.setItems(tasks);

        //every time a task is selected on the list, changed method is called and reassigns the selected task with the chosen one
        taskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observableValue, Task task, Task t1) {
            }
        });

    }

    @FXML
    void assignTaskButtonPressed(ActionEvent event) {
        selectedTask = taskList.getSelectionModel().getSelectedItem();
        if(selectedTask == null){
            errorMessageLabel.setText("Please Select a Task");
        }else {
            currentTaskLabel.setText("Current Task: " + selectedTask.getDescription());
            errorMessageLabel.setText("");
        }
    }

    /**
     * takes a total number of seconds as a parameter and update the timers hours, minutes and seconds accordingly
     *
     * @param totalSeconds total seconds of new timer
     */
    private void updateTimerFromTotal(int totalSeconds) {
        timer.setHours(totalSeconds / 3600);
        timer.setMinutes((totalSeconds % 3600) / 60);
        timer.setSeconds(totalSeconds % 60);
        timerText.setText(timer.getCurrentTime());
    }

    /**
     *
     * @param event add time button click
     */
    @FXML
    void plusSeconds(ActionEvent event) {
        updateTimerFromTotal(timer.getTotalSeconds() + 5);
    }

    @FXML
    void plusMinutes(ActionEvent event) {
        updateTimerFromTotal(timer.getTotalSeconds() + 300);
    }

    @FXML
    void plusHours(ActionEvent event) {
        updateTimerFromTotal(timer.getTotalSeconds() + 3600);
    }

    @FXML
    void minusSeconds(ActionEvent event) {
        if (timer.getTotalSeconds() - 5 < 0) {
            errorMessageLabel.setText("Cannot Set Time Below Zero");
            clearTimer();
            return;
        }
        updateTimerFromTotal(timer.getTotalSeconds() - 5);
    }

    @FXML
    void minusMinutes(ActionEvent event) {
        if (timer.getTotalSeconds() - 300 < 0) {
            errorMessageLabel.setText("Cannot Set Time Below Zero");
            clearTimer();
            return;
        }
        updateTimerFromTotal(timer.getTotalSeconds() - 300);
    }

    @FXML
    void minusHours(ActionEvent event) {
        if (timer.getTotalSeconds() - 3600 < 0) {
            errorMessageLabel.setText("Cannot Set Time Below Zero");
            clearTimer();
            return;
        }
        updateTimerFromTotal(timer.getTotalSeconds() - 3600);
    }

    private void tick() {

        timer.oneSecondPassed();
        timerText.setText(timer.getCurrentTime());

        //stops timer when seconds, minutes and hours are 0
        if (timer.getTotalSeconds() == 0) {
            selectedTask.setIsComplete(true);
            tm.updateTask();
            shop.updateTotalCoins(50);
            pauseTimer();
        }
    }

    @FXML
    void startTimer(ActionEvent event) {

        if(selectedTask == null){
            errorMessageLabel.setText("Please assign a Task To Complete");
        }else if(timer.getTotalSeconds() == 0){
            errorMessageLabel.setText("Please enter an amount of time");
        }else{
            errorMessageLabel.setText("");
            timeline.stop();
            timeline.playFromStart();
            disableButtons(true);
        }
    }

    @FXML
    void pauseTimer(){
        timeline.stop();
        disableButtons(false);
    }

    @FXML
    void clearTimer(){
        //resets timer and sets all values to 0
        timer.setSeconds(0);
        timer.setMinutes(0);
        timer.setHours(0);
        timerText.setText(timer.getCurrentTime());
        disableButtons(false);
    }

    public void disableButtons(boolean enabled){
        plusSecondsButton.setDisable(enabled);
        plusMinutesButton.setDisable(enabled);
        plusHoursButton.setDisable(enabled);
        minusSecondsButton.setDisable(enabled);
        minusMinutesButton.setDisable(enabled);
        minusHoursButton.setDisable(enabled);
        taskList.setDisable(enabled);
        assignTaskButton.setDisable(enabled);
    }



}

