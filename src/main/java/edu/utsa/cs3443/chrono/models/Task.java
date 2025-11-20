package edu.utsa.cs3443.chrono.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;


/**
 * Class to define the structure of a task and any properties it has.
 *
 * @author Collin Schiebel
 */
public class Task {

    private final StringProperty description;
    private final BooleanProperty isComplete;
    private final IntegerProperty pointValue;

    private final ObjectProperty<LocalDate> dueDate;
    private final StringProperty timeEstimate;
    private final StringProperty autoCompleteTime;
    private final StringProperty notes;

    public Task(String description, int pointValue) {
        this.description = new SimpleStringProperty(description);
        this.isComplete = new SimpleBooleanProperty(false);
        this.pointValue = new SimpleIntegerProperty(pointValue);

        // Initialize defaults
        this.dueDate = new SimpleObjectProperty<>(LocalDate.now().plusDays(1));
        this.timeEstimate = new SimpleStringProperty("1 hour");
        this.autoCompleteTime = new SimpleStringProperty("Never");
        this.notes = new SimpleStringProperty("");
    }

    // --- Property Getters ---
    public StringProperty descriptionProperty() { return description; }
    public BooleanProperty isCompleteProperty() { return isComplete; }
    public IntegerProperty pointValueProperty() { return pointValue; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public StringProperty timeEstimateProperty() { return timeEstimate; }
    public StringProperty autoCompleteTimeProperty() { return autoCompleteTime; }
    public StringProperty notesProperty() { return notes; }

    // --- Standard Getters ---
    public String getDescription() { return description.get(); }
    public boolean isComplete() { return isComplete.get(); }
    public LocalDate getDueDate() { return dueDate.get(); }

    // --- Setters ---
    public void setDescription(String description) { this.description.set(description); }
    public void setIsComplete(boolean isComplete) { this.isComplete.set(isComplete); }

    @Override
    public String toString() {
        return getDescription(); // Safety fallback
    }
}