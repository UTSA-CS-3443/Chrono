package edu.utsa.cs3443.chrono;

import javafx.event.ActionEvent;
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
    private Button darkThemeButton;

    @FXML
    private String selectedTheme;
    private String activeTheme;
    private String priorTheme;

    private mainUI_Controller mc;

    public void setMainController(mainUI_Controller controller) {
        this.mc = controller;
    }


    @FXML
    public void initialize(){
        loadThemeButtons();
        selectedTheme = "default.css";
        priorTheme = "default.css";
    }

    @FXML
    void greenThemePressed(ActionEvent event) {
        selectedTheme = "green.css";
    }

    @FXML
    void defaultPressed(ActionEvent event) {
        selectedTheme = "default.css";
    }
    @FXML
    void darkThemePressed(ActionEvent event) {
        selectedTheme = "dark.css";
    }

    void changeTheme(String newCSS,String oldCSS){
        ChronoApplication.sceneReference.getStylesheets().remove(ChronoApplication.class.getResource("css/" + oldCSS).toExternalForm());
        ChronoApplication.sceneReference.getStylesheets().add(ChronoApplication.class.getResource("css/" + newCSS).toExternalForm());
    }

    @FXML
    void applyTheme(ActionEvent event) {
        System.out.println(priorTheme);
        changeTheme(selectedTheme,priorTheme);
        activeTheme = selectedTheme;
        priorTheme = activeTheme;
    }

    void loadThemeButtons(){
        redThemeButton.getStyleClass().add("red-theme-button");
        defaultThemeButton.getStyleClass().add("default-theme-button");
        greenThemeButton.getStyleClass().add("green-theme-button");
        darkThemeButton.getStyleClass().add("dark-theme-button");

        //put all buttons into an array list and sort through it to check which are disabled and unlocked them accordingly

        if(redThemeButton.isDisabled()){
            redThemeButton.setText("LOCKED");
        }

        if(greenThemeButton.isDisabled()){
            greenThemeButton.setText("LOCKED");
        }


    }

}
