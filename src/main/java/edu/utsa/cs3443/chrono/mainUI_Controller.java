package edu.utsa.cs3443.chrono;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import java.io.IOException;


public class mainUI_Controller {

    @FXML
    private BorderPane layoutPane;

    @FXML
    private void initialize(){
        // set the creature view to load on startup
        loadCenter("store_view.fxml");
    }

    private void loadCenter(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/utsa/cs3443/chrono/layouts/" + fxmlFileName)
            );

            if (loader.getLocation() == null) {
                System.err.println("FXML not found: " + fxmlFileName);
                return;
            }

            layoutPane.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goChecklist(ActionEvent actionEvent) {
        loadCenter("checklist_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Checklist");
        
    }

    public void goTimer(ActionEvent actionEvent) {
        loadCenter("timer_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Task Timer");

    }

    public void goCreature(ActionEvent actionEvent) {
        loadCenter("creature_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Creature");

    }

    public void goThemes(ActionEvent actionEvent) {
        loadCenter("themes_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Themes");

    }

    public void goStore(ActionEvent actionEvent) throws IOException {
        loadCenter("store_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Store");

    }
}
