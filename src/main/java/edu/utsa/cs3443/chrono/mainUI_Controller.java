package edu.utsa.cs3443.chrono;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import javafx.scene.control.Label;

public class mainUI_Controller {

    @FXML
    private Label headerLabel;

    @FXML
    private BorderPane layoutPane;

    @FXML
    private void initialize(){
        // set the welcome view to load on startup
        loadCenter("welcome_view.fxml");
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

            Object controller = loader.getController();
            if (controller instanceof WelcomeController wc) {
                wc.setMainController(this);
            }
            if (controller instanceof ThemesController tc) {
                tc.setMainController(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goChecklist(ActionEvent actionEvent) {
        loadCenter("checklist_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Chrono - Checklist");
        headerLabel.setText("Checklist");

    }

    public void goTimer(ActionEvent actionEvent) {
        loadCenter("timer_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Chrono - Task Timer");
        headerLabel.setText("Task Timer");

    }

    public void goCreature(ActionEvent actionEvent) {
        loadCenter("creature_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Chrono - Creature");
        headerLabel.setText("Creature");

    }

    public void goThemes(ActionEvent actionEvent) {
        loadCenter("themes_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Chrono - Themes");
        headerLabel.setText("Themes");

    }

    public void goStore(ActionEvent actionEvent) throws IOException {
        loadCenter("store_view.fxml");
        ChronoApplication.primaryStageReference.setTitle("Chrono - Store");
        headerLabel.setText("Store");

    }

}
