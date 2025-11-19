package edu.utsa.cs3443.chrono;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ThemesController {

    @FXML
    private Button redThemeButton;

    @FXML
    private Button defaultThemeButton;

    @FXML
    private Button greenThemeButton;

    @FXML
    public void initialize(){
        redThemeButton.getStyleClass().add("red-theme-button");
        defaultThemeButton.getStyleClass().add("default-theme-button");
        greenThemeButton.getStyleClass().add("green-theme-button");
    }
}
