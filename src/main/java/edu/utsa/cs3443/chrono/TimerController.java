package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.TimerModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerController {

    @FXML
    private Label timerText;
    private TimerModel timer;
    private Timeline timeline;

    @FXML
    public void initialize() {
        //create new timer object with default values of 0
        timer = new TimerModel(0, 0, 0);


        timerText.setText(timer.getCurrentTime());

        //new timeline and keyframe which calls every second until stopped
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * utilized to add time to the timer, will be updated to add values greater than 5 seconds as well as remove time
     * calculates total seconds and assigns proper values to each respective variable
     * @param event add time button click
     */
    @FXML
    void plusSeconds(ActionEvent event) {

        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds()
                + 5;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        timer.setHours(hours);
        timer.setMinutes(minutes);
        timer.setSeconds(seconds);

        timerText.setText(timer.getCurrentTime());

    }

    @FXML
    void minusHours(ActionEvent event) {
        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds();

        if(totalSeconds - 3600 >= 0){
            totalSeconds -= 3600;
            timer.setHours(totalSeconds / 3600);
            timer.setMinutes((totalSeconds % 3600) / 60);
            timer.setSeconds(totalSeconds % 60);
        } else{

        }

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void minusMinutes(ActionEvent event) {
        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds();

        if(totalSeconds - 300 >= 0){
            totalSeconds -= 300;
            timer.setHours(totalSeconds / 3600);
            timer.setMinutes((totalSeconds % 3600) / 60);
            timer.setSeconds(totalSeconds % 60);
        } else{

        }

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void minusSeconds(ActionEvent event) {

        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds();

        if(totalSeconds - 5 >= 0){
            totalSeconds -= 5;
            timer.setHours(totalSeconds / 3600);
            timer.setMinutes((totalSeconds % 3600) / 60);
            timer.setSeconds(totalSeconds % 60);
        } else{

        }

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void plusHours(ActionEvent event) {
        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds()
                + 3600;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        timer.setHours(hours);
        timer.setMinutes(minutes);
        timer.setSeconds(seconds);

        timerText.setText(timer.getCurrentTime());
    }

    @FXML
    void plusMinutes(ActionEvent event) {
        int totalSeconds = timer.getHours() * 3600
                + timer.getMinutes() * 60
                + timer.getSeconds()
                + 300;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        timer.setHours(hours);
        timer.setMinutes(minutes);
        timer.setSeconds(seconds);

        timerText.setText(timer.getCurrentTime());
    }

    private void tick() {

        timer.oneSecondPassed();
        timerText.setText(timer.getCurrentTime());

        //stops timer when seconds, minutes and hours are 0
        if (timer.getHours() == 0 && timer.getMinutes() == 0 && timer.getSeconds() == 0) {
            pauseTimer();
            return;
        }
    }

    @FXML
    void startTimer(ActionEvent event) {
        //idk why this works but it does, makes no sense to me but i dont wanna mess with it
        timeline.stop();
        timeline.playFromStart();
    }

    @FXML
    void pauseTimer(){
        timeline.stop();
    }

    @FXML
    void clearTimer(){
        //resets timer and sets all values to 0
        timer.setSeconds(0);
        timer.setMinutes(0);
        timer.setHours(0);
        timerText.setText(timer.getCurrentTime());
    }
}

