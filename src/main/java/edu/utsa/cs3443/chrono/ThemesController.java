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
        if(redThemeButton.isDisabled()){
            redThemeButton.setText("LOCKED");
        }
        if(greenThemeButton.isDisabled()){
            greenThemeButton.setText("LOCKED");
        }
        greenThemeButton.getStyleClass().add("green-theme-button");
    }
}
