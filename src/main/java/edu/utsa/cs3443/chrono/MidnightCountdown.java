package edu.utsa.cs3443.chrono;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class MidnightCountdown {
    private final AnimationTimer timer;
    @FXML
    private final Label timeLabel;
    @FXML
    private final ProgressBar progressBar;

    public MidnightCountdown(Label timeLabel, ProgressBar progressBar){
        this.timeLabel = timeLabel;
        this.progressBar = progressBar;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCountdown();
            }
        };
    }

    public void start(){
        updateCountdown();
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    private long secondsUntilMidnight(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, midnight).getSeconds();
    }

    private void updateCountdown(){
        long secondsUntilMidnight = secondsUntilMidnight();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(); // Next midnight

        Duration duration = Duration.between(now, midnight);

        long hours = secondsUntilMidnight / 3600;
        long minutes = (secondsUntilMidnight % 3600) / 60;
        long seconds = secondsUntilMidnight % 60;

        String timeText = String.format("Time left: %02d hrs, %02d mins, %02d secs", hours, minutes, seconds);
        timeLabel.setText(timeText);



        double progress = 1.0 - (secondsUntilMidnight / 86400.0);
        progressBar.setProgress(progress);
    }
}
