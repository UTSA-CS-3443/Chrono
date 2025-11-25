package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Theme;
import edu.utsa.cs3443.chrono.models.UnlockableManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;


public class ThemesController {

    @FXML
    private FlowPane themeButtonBox;

    @FXML
    private String selectedTheme;
    private String activeTheme;
    private String priorTheme;

    private mainUI_Controller mc;
    private UnlockableManager um;
    private ArrayList<Theme> themeList;

    public void setMainController(mainUI_Controller controller) {
        this.mc = controller;
    }



    @FXML
    public void initialize(){
        System.out.println(themeList);
        um = new UnlockableManager();
        themeList = um.getThemeList();
        createButtons();
        selectedTheme = "default.css";
        priorTheme = "default.css";
    }

    @FXML
   public void createButtons(){

        for(Theme theme : themeList) {
            Button button = new Button(theme.getName());
            button.getStyleClass().add(theme.getButtonTheme());

            button.setPrefWidth(550);
            button.setPrefHeight(50);


            button.setOnAction(e->{
                selectedTheme = theme.getThemeCSS();
                System.out.println("selected: " + theme.getName());
            });

            themeButtonBox.getChildren().add(button);
        }
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
        changeTheme(selectedTheme,priorTheme);
        activeTheme = selectedTheme;
        priorTheme = activeTheme;
    }

}
