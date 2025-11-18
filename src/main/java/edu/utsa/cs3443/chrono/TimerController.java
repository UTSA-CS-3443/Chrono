package edu.utsa.cs3443.chrono;

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

/*
TODO add button to confirm confirmation of selected task
TODO when timer runs out, auto complete task, or ask user if task is completed
TODO add due date, total time, other things to task objects
TODO grey out assign and list when start button is pressed to prevent changing while timer runs
TODO reset timer if new task is selected after time start,
TODO warn user this will reset timer when they go to assign it, ask for confirmation
 */

/**
 * controller class for operations on timer screen
 *
 * @author Davis Howe
 */
public class TimerController {

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

    private TaskManager tm;

    private TimerModel timer;
    private Timeline timeline;
    private ObservableList<Task> tasks;
    private Task selectedTask;

    @FXML
    public void initialize() {
        //create new timer object with default values of 0
        timer = new TimerModel(0, 0, 0);

        timerText.setText(timer.getCurrentTime());

        //new timeline and keyframe which calls every second until stopped
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        tm = new TaskManager();
        tm.addTask("test 1", 10);
        tm.addTask("test 2", 20);
        tasks = FXCollections.observableArrayList();
        tasks.addAll(tm.getTasks());
        taskList.setItems(tasks);

        //every time a task is selected on the list, changed method is called and reassigns the selected task with the chosen one
        taskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observableValue, Task task, Task t1) {

                selectedTask = taskList.getSelectionModel().getSelectedItem();

                currentTaskLabel.setText("Current Task: " + selectedTask.getDescription());

            }
        });

    }



    /**
     * utilized to add time to the timer, will be updated to add values greater than 5 seconds as well as remove time
     * calculates total seconds and assigns proper values to each respective variable
     * @param event add time button click
     */
    @FXML
    void plusSeconds(ActionEvent event) {

        timer.setHours((timer.getTotalSeconds() + 5) / 3600);
        timer.setMinutes(((timer.getTotalSeconds() + 5) % 3600) / 60);
        timer.setSeconds((timer.getTotalSeconds() + 5) % 60);

        timerText.setText(timer.getCurrentTime());

    }

    @FXML
    void minusHours(ActionEvent event) {

        if(timer.getTotalSeconds() - 3600 >= 0){
            timer.setHours((timer.getTotalSeconds() - 3600) / 3600);
            timer.setMinutes(((timer.getTotalSeconds() - 3600) % 3600) / 60);
            timer.setSeconds((timer.getTotalSeconds() - 3600) % 60);
        } else{
            errorMessageLabel.setText("Cannot Set Time Below Zero");
            clearTimer();
        }

        timerText.setText(timer.getCurrentTime());
    }

    //TODO fix bug where timer reduces to negatives when subtracting minutes from 1hr exactly
    @FXML
    void minusMinutes(ActionEvent event) {
        if(timer.getTotalSeconds() - 300 >= 0){
            timer.setHours((timer.getTotalSeconds() - 300) / 3600);
            timer.setMinutes(((timer.getTotalSeconds() - 300) % 3600) / 60);
            timer.setSeconds((timer.getTotalSeconds() - 300) % 60);
        } else{
            clearTimer();
            errorMessageLabel.setText("Cannot Set Time Below Zero");

        }

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void minusSeconds(ActionEvent event) {

        if(timer.getTotalSeconds() - 5 >= 0){
            timer.setHours((timer.getTotalSeconds() - 5) / 3600);
            timer.setMinutes(((timer.getTotalSeconds() - 5) % 3600) / 60);
            timer.setSeconds((timer.getTotalSeconds() - 5) % 60);
        } else{
            errorMessageLabel.setText("Cannot Set Time Below Zero");
            clearTimer();
        }

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void plusHours(ActionEvent event) {

        timer.setHours((timer.getTotalSeconds() + 3600) / 3600);
        timer.setMinutes(((timer.getTotalSeconds() + 3600) % 3600) / 60);
        timer.setSeconds((timer.getTotalSeconds() + 3600) % 60);

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void plusMinutes(ActionEvent event) {

        timer.setHours((timer.getTotalSeconds() + 300) / 3600);
        timer.setMinutes(((timer.getTotalSeconds() + 300) % 3600) / 60);
        timer.setSeconds((timer.getTotalSeconds() + 300) % 60);

        timerText.setText(timer.getCurrentTime());
    }

    private void tick() {

        timer.oneSecondPassed();
        timerText.setText(timer.getCurrentTime());

        //stops timer when seconds, minutes and hours are 0
        if (timer.getTotalSeconds() == 0) {
            pauseTimer();
        }
    }

    @FXML
    void startTimer(ActionEvent event) {

        if(timer.getTotalSeconds() == 0){
            errorMessageLabel.setText("Please enter an amount of time");
        }else{
            //idk why this works but it does, makes no sense to me but i dont wanna mess with it
            errorMessageLabel.setText("");
            timeline.stop();
            timeline.playFromStart();
            plusSecondsButton.setDisable(true);
            plusMinutesButton.setDisable(true);
            plusHoursButton.setDisable(true);
            minusSecondsButton.setDisable(true);
            minusMinutesButton.setDisable(true);
            minusHoursButton.setDisable(true);
        }
    }

    @FXML
    void pauseTimer(){
        timeline.stop();
        plusSecondsButton.setDisable(false);
        plusMinutesButton.setDisable(false);
        plusHoursButton.setDisable(false);
        minusSecondsButton.setDisable(false);
        minusMinutesButton.setDisable(false);
        minusHoursButton.setDisable(false);
    }

    @FXML
    void clearTimer(){
        //resets timer and sets all values to 0
        timer.setSeconds(0);
        timer.setMinutes(0);
        timer.setHours(0);
        timerText.setText(timer.getCurrentTime());
        plusSecondsButton.setDisable(false);
        plusMinutesButton.setDisable(false);
        plusHoursButton.setDisable(false);
        minusSecondsButton.setDisable(false);
        minusMinutesButton.setDisable(false);
        minusHoursButton.setDisable(false);
    }




}

