package edu.utsa.cs3443.chrono.models;

/**
 * class to create a new timer object to be utilized on the timer screen
 *
 * @author Davis Howe
 */
public class TimerModel {

    private int seconds;
    private int minutes;
    private int hours;

    public TimerModel(int hours, int minutes, int seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * formats time into a string
     *
     * @return string to be displayed on timer screen
     */
    public String getCurrentTime(){
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * called every second, de-increments and updates time accordingly
     */
    public void oneSecondPassed(){

        //stops de-incrementing when timer hits 0
        if (hours == 0 && minutes == 0 && seconds == 0) {
            return;
        }

        //while seconds is greater than 0, it will be de-incremented, otherwise it is set to 59
        if (seconds > 0) {
            seconds--;
            return;
        }

        seconds = 59;
        //while minutes is greater than 0, it will be de-incremented, otherwise it is set to 59
        if (minutes > 0) {
            minutes--;
            return;
        }

        minutes = 59;
        hours--;
    }


    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getTotalSeconds(){
        return (hours * 3600) + (minutes * 60) + seconds;
    }


}
