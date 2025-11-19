package edu.utsa.cs3443.chrono.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The Task class represents a single to-do item.
 * It holds its description, completion state, and point value
 * using JavaFX properties for easy data binding with the UI.
 *
 * @author Collin Schiebel
 */
public class Task {

    private final StringProperty description;
    private final BooleanProperty isComplete;
    private final IntegerProperty pointValue;

    /**
     * Constructor for a new Task.
     *
     * @param description The text description of the task.
     * @param pointValue  The number of points/coins awarded for completion.
     */
    public Task(String description, int pointValue) {
        this.description = new SimpleStringProperty(description);
        this.isComplete = new SimpleBooleanProperty(false); // Tasks start as incomplete
        this.pointValue = new SimpleIntegerProperty(pointValue);
    }

    //Property Getters
    // These methods return the JavaFX property itself, which is needed for data binding.

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty isCompleteProperty() {
        return isComplete;
    }

    public IntegerProperty pointValueProperty() {
        return pointValue;
    }

    //Standard Getters
    // These return the raw value (String, boolean, int).

    public String getDescription() {
        return description.get();
    }

    public boolean isComplete() {
        return isComplete.get();
    }

    public int getPointValue() {
        return pointValue.get();
    }

    //Standard Setters
    // These set the raw value.

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete.set(isComplete);
    }

    public void setPointValue(int pointValue) {
        this.pointValue.set(pointValue);
    }

    @Override
    public String toString() {
        return description + " - " + pointValue;
    }
}