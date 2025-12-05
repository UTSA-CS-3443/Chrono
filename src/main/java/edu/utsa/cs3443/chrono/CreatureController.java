package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Cosmetic;
import edu.utsa.cs3443.chrono.models.CreatureModel;
import edu.utsa.cs3443.chrono.models.ShopModel;
import edu.utsa.cs3443.chrono.models.UnlockableManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;

/**
 * Controller for the Creature Screen.
 * Handles creature interactions (feed/play), state decay, and cosmetic equipment.
 *
 * @author Collin Schiebel
 */
public class CreatureController {

    @FXML private ProgressBar healthBar;
    @FXML private ProgressBar happinessBar;

    @FXML private Label coinLabel;
    @FXML private Label healthPercentLabel;
    @FXML private Label happinessPercentLabel;

    @FXML private Button feedButton;
    @FXML private Button playButton;

    @FXML private ImageView creatureImageView;
    @FXML private VBox cosmeticsContainer;

    private CreatureModel creatureModel;
    private ShopModel shopModel;
    private UnlockableManager unlockableManager;
    private mainUI_Controller mainController;

    private Timeline decayTimer;
    private Cosmetic equippedCosmetic = null;

    /**
     * Initializes models, binds UI elements, loads cosmetics, and starts the decay timer.
     */
    @FXML
    public void initialize() {
        this.creatureModel = new CreatureModel();
        this.shopModel = new ShopModel();
        this.unlockableManager = new UnlockableManager();

        healthBar.progressProperty().bind(creatureModel.healthProperty());
        happinessBar.progressProperty().bind(creatureModel.happinessProperty());

        healthPercentLabel.textProperty().bind(
                creatureModel.healthProperty().multiply(100).asString("%.0f%%")
        );
        happinessPercentLabel.textProperty().bind(
                creatureModel.happinessProperty().multiply(100).asString("%.0f%%")
        );

        updateCoinLabel();
        startDecayTimer();
        loadUnlockedCosmetics();
    }

    /**
     * Loads purchased cosmetics from the manager and populates the sidebar.
     */
    private void loadUnlockedCosmetics() {
        cosmeticsContainer.getChildren().clear();
        ArrayList<Cosmetic> allCosmetics = unlockableManager.getCosmeticList();

        for (Cosmetic cosmetic : allCosmetics) {
            if (cosmetic.isUnlocked()) {
                VBox itemBox = createCosmeticItemBox(cosmetic);
                cosmeticsContainer.getChildren().add(itemBox);
            }
        }
    }

    /**
     * Creates a UI element for a single cosmetic item, including its image and equip button.
     */
    private VBox createCosmeticItemBox(Cosmetic cosmetic) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        Button imageBtn = new Button();
        imageBtn.setPrefSize(150, 150);

        String imagePath = "images/" + cosmetic.getImageFilePath();
        URL resource = getClass().getResource(imagePath);

        if (resource != null) {
            imageBtn.setStyle(
                    "-fx-background-image: url('" + resource.toExternalForm() + "');" +
                            "-fx-background-size: cover;" +
                            "-fx-background-position: center;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-cursor: default;"
            );
        } else {
            imageBtn.setText("Img Missing");
        }

        Button actionBtn = new Button("Equip");
        actionBtn.setPrefWidth(150);
        actionBtn.getStyleClass().add("action-button");

        actionBtn.setOnAction(e -> handleEquipToggle(cosmetic, actionBtn));

        box.getChildren().addAll(imageBtn, actionBtn);
        return box;
    }

    /**
     * Toggles the equip state of a cosmetic item.
     * Enforces single-item equipment rule.
     */
    private void handleEquipToggle(Cosmetic selectedCosmetic, Button btn) {
        if (equippedCosmetic == selectedCosmetic) {
            unequipCurrent();
            btn.setText("Equip");
            btn.setStyle("");
        }
        else {
            if (equippedCosmetic != null) {
                showAlert("Item Equipped", "You must unequip your current item before equipping a new one!");
            }
            else {
                equipItem(selectedCosmetic);
                btn.setText("Unequip");
                btn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
            }
        }
    }

    /**
     * Updates the main creature image to the variant associated with the cosmetic.
     */
    private void equipItem(Cosmetic cosmetic) {
        equippedCosmetic = cosmetic;

        String filename = getEquippedImageName(cosmetic.getName());
        String path = "images/" + filename;

        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("CRITICAL ERROR: Could not find equipped image: " + path);
                unequipCurrent();
                return;
            }
            creatureImageView.setImage(new Image(resource.toExternalForm()));

        } catch (Exception e) {
            e.printStackTrace();
            unequipCurrent();
        }
    }

    /**
     * Maps cosmetic names to their specific file names.
     */
    private String getEquippedImageName(String cosmeticName) {
        String normalized = cosmeticName.trim().toLowerCase();

        if (normalized.contains("boots")) {
            return "Creature_Boots_Equipped.png";
        }
        else if (normalized.contains("shirt") || normalized.contains("t-shirt")) {
            return "Creature_Shirt_Equipped.png";
        }
        else if (normalized.contains("wizard")) {
            return "Creature_WizardHat_Equipped.png";
        }
        else if (normalized.contains("hat")) {
            return "Creature_Hat_Equipped.png";
        }

        return "Creature_" + cosmeticName.replace(" ", "") + "_Equipped.png";
    }

    private void unequipCurrent() {
        equippedCosmetic = null;
        try {
            String defaultUrl = getClass().getResource("images/Creature_Idle_Default.png").toExternalForm();
            creatureImageView.setImage(new Image(defaultUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCoinLabel() {
        if (coinLabel != null) {
            coinLabel.setText("x " + shopModel.getTotalCoins());
        }
    }

    /**
     * Starts a background timer that decreases health and happiness over time.
     */
    private void startDecayTimer() {
        decayTimer = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            double currentHealth = creatureModel.getHealth();
            double currentHappy = creatureModel.getHappiness();

            creatureModel.setHealth(Math.max(0.0, currentHealth - 0.05));
            creatureModel.setHappiness(Math.max(0.0, currentHappy - 0.05));
        }));
        decayTimer.setCycleCount(Timeline.INDEFINITE);
        decayTimer.play();
    }

    public void setMainController(mainUI_Controller controller) {
        this.mainController = controller;
    }

    @FXML
    private void handleFeed() {
        if (creatureModel.getHealth() >= 1.0) return;

        int cost = 5;
        if (shopModel.getTotalCoins() >= cost) {
            shopModel.updateTotalCoins(-cost);
            creatureModel.feed();
            updateCoinLabel();
        } else {
            showAlert("Not Enough Coins", "You need 5 coins to feed your creature.");
        }
    }

    @FXML
    private void handlePlay() {
        if (creatureModel.getHappiness() >= 1.0) return;

        int cost = 5;
        if (shopModel.getTotalCoins() >= cost) {
            shopModel.updateTotalCoins(-cost);
            creatureModel.water();
            updateCoinLabel();
        } else {
            showAlert("Not Enough Coins", "You need 5 coins to play with your creature.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void stopTimer() {
        if (decayTimer != null) decayTimer.stop();
    }
}