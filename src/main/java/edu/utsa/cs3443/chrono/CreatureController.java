package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.CreatureModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import java.io.IOException; // Added for exception handling

/**
 * Controller for the Creature Screen.
 *
 * @author Collin Schiebel
 */
public class CreatureController {

    @FXML private ProgressBar healthBar;
    @FXML private ProgressBar happinessBar;
    @FXML private ProgressBar coinsBar;

    @FXML private Button feedButton;
    @FXML private Button playButton;
    @FXML private Button storeButton;

    private CreatureModel creatureModel;
    private mainUI_Controller mainController; // Reference to main controller

    @FXML
    public void initialize() {
        this.creatureModel = new CreatureModel();

        // Bind the Progress Bars to the Model
        healthBar.progressProperty().bind(creatureModel.healthProperty());
        happinessBar.progressProperty().bind(creatureModel.happinessProperty());
        coinsBar.progressProperty().bind(creatureModel.coinsProperty().divide(100.0));
    }

    // Method to allow the main controller to pass a reference to itself
    public void setMainController(mainUI_Controller controller) {
        this.mainController = controller;
    }

    @FXML
    private void handleFeed() {
        creatureModel.feed();
        System.out.println("Fed creature. Health is now: " + creatureModel.getHealth());
    }

    @FXML
    private void handlePlay() {
        creatureModel.water();
        System.out.println("Played with creature. Happiness is now: " + creatureModel.getHappiness());
    }

    @FXML
    private void handleStore() {
        if (mainController != null) {
            try {
                // Call the existing goStore logic from the main controller
                mainController.goStore(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}