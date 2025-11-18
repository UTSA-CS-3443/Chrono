package edu.utsa.cs3443.chrono;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChronoApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ChronoApplication.class.getResource("/edu/utsa/cs3443/chrono/layouts/main_ui.fxml")  // Make sure this filename is EXACT
        );

        Scene scene = new Scene(loader.load(), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chrono");
        primaryStage.setResizable(true);  // optional
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}