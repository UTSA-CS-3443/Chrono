package edu.utsa.cs3443.chrono;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.util.Objects;

public class ThemesController {

    @FXML
    private Button redThemeButton;

    @FXML
    private Button defaultThemeButton;

    @FXML
    private Button greenThemeButton;

    @FXML
    private Button applyTheme;
    private boolean greenSelected;
    private String selectedTheme;
    private String activeTheme;
    private String priorTheme;

    private mainUI_Controller mc;

    public void setMainController(mainUI_Controller controller) {
        this.mc = controller;
    }


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
        selectedTheme = "default.css";
        priorTheme = "default.css";
    }

    @FXML
    void greenThemePressed(ActionEvent event) {
        greenSelected = true;
        selectedTheme = "green.css";

    }

    @FXML
    void defaultPressed(ActionEvent event) {
        greenSelected = false;
        selectedTheme = "default.css";
    }

    void changeTheme(String newCSS,String oldCSS){
        ChronoApplication.sceneReference.getStylesheets().remove(ChronoApplication.class.getResource("layouts/" + oldCSS).toExternalForm());
        ChronoApplication.sceneReference.getStylesheets().add(ChronoApplication.class.getResource("layouts/" + newCSS).toExternalForm());
    }

    @FXML
    void applyTheme(ActionEvent event) {
        System.out.println(priorTheme);
        changeTheme(selectedTheme,priorTheme);
        activeTheme = selectedTheme;
        priorTheme = activeTheme;

        if (mc == null) {
            System.out.println("Main controller not set");
            return;
        }

        if (mc.getSidebarVbox() == null) {
            System.out.println("sidebarVbox not injected!");
            return;
        }

        if(greenSelected) {
            mc.getSidebarVbox().setStyle("-fx-background-color: #1B4F22;");
            mc.getHeader().setStyle("-fx-background-color: #1B4F22;");
            mc.getChecklistButton().setStyle("-fx-background-color: #1B4F22;");
            mc.getTimerButton().setStyle("-fx-background-color: #1B4F22;");
            mc.getStoreButton().setStyle("-fx-background-color: #1B4F22;");
            mc.getCreatureButton().setStyle("-fx-background-color: #1B4F22;");
            mc.getThemesButton().setStyle("-fx-background-color: #1B4F22;");
            applyTheme.getStyleClass().add("green-button");
        }else{
            mc.getSidebarVbox().setStyle("-fx-background-color: #193339;");
            mc.getHeader().setStyle("-fx-background-color: #193339;");
            mc.getChecklistButton().setStyle("-fx-background-color: #193339;");
            mc.getTimerButton().setStyle("-fx-background-color: #193339;");
            mc.getStoreButton().setStyle("-fx-background-color: #193339;");
            mc.getCreatureButton().setStyle("-fx-background-color: #193339;");
            mc.getThemesButton().setStyle("-fx-background-color: #193339;");
            applyTheme.getStyleClass().remove("green-button");
        }

    }



}
