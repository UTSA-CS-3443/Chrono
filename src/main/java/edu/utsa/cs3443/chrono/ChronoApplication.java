package edu.utsa.cs3443.chrono;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChronoApplication extends Application {
    static Stage primaryStageReference;
    static Scene sceneReference;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ChronoApplication.class.getResource("/edu/utsa/cs3443/chrono/layouts/main_ui.fxml")// Make sure this filename is EXACT
        );
        primaryStageReference = primaryStage;
        Scene scene = new Scene(loader.load());
        sceneReference = scene;
        scene.getStylesheets().add(ChronoApplication.class.getResource("css/default.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chrono");
        primaryStage.setResizable(true);  // optional
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}