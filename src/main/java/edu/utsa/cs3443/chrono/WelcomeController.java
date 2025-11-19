package edu.utsa.cs3443.chrono;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;

public class WelcomeController {

    private mainUI_Controller mainController;

    // This will be called by mainUI after FXML load
    public void setMainController(mainUI_Controller controller) {
        this.mainController = controller;
    }

    @FXML
    void viewChecklist(ActionEvent event) {
        if (mainController != null) {
            mainController.goChecklist(event);
        } else {
            System.err.println("mainController is null!");
        }
    }
}
