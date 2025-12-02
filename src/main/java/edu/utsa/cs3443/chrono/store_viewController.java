package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Cosmetic;
import edu.utsa.cs3443.chrono.models.ShopModel;
import edu.utsa.cs3443.chrono.models.Theme;
import edu.utsa.cs3443.chrono.models.UnlockableManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;

public class store_viewController{
    public ProgressBar midnightProgress;

    @FXML
    public Label coinCounter;
    @FXML
    private Label timeLabel;
    @FXML
    private FlowPane themeButtonBox;
    @FXML
    private FlowPane cosmeticButtonBox;

    // These buttons do not do anything when clicked, they are just to show the user what needs to be done
    @FXML
    private Button task1;
    @FXML
    private Button task2;
    @FXML
    private Button task3;



    private final ArrayList<String> dailyTasks = new ArrayList<String>();
    private ArrayList<Theme> themeList;
    private ArrayList<Cosmetic> cosmeticsList;
    private UnlockableManager um;
    private ShopModel shop;

    @FXML
    private void initialize(){
        shop = new ShopModel();
        getDailies();
        loadOrResetDailyProgress();
        MidnightCountdown countdown = new MidnightCountdown(timeLabel, midnightProgress);
        countdown.start();
        updateCoinCounter();
        um = new UnlockableManager();
        themeList = um.getThemeList();
        cosmeticsList = um.getCosmeticList();
        createThemeButtons();
        createCosmeticButtons();
    }

    @FXML
    public void createThemeButtons(){
        for(Theme theme : themeList) {
            if(!theme.isUnlocked()) {
                Button button = new Button(theme.getName());

                button.getStyleClass().add(theme.getButtonTheme());
                button.setText("LOCKED - " + theme.getCost() + " Coins");

                button.setPrefWidth(300);
                button.setPrefHeight(50);

                button.setOnAction(e -> {
                    if(unlockTheme(theme)){
                        themeButtonBox.getChildren().remove(button);
                    }
                });

                themeButtonBox.getChildren().add(button);
            }
        }
    }

    @FXML
    public void createCosmeticButtons(){
        for(Cosmetic cosmetic : cosmeticsList) {
            Button button = new Button(cosmetic.getName());


            String imageUrl = getClass().getResource("images/" + cosmetic.getImageFilePath()).toExternalForm();

            button.setStyle(
                    "-fx-background-image: url('" + imageUrl + "');" + "-fx-background-size: cover;" + "-fx-background-position: center;" + "-fx-background-repeat: no-repeat;"
            );

            button.setText("LOCKED - " + cosmetic.getCost() + " Coins");

            button.setPrefWidth(150);
            button.setPrefHeight(150);

            button.setOnAction(e->{

                if(unlockCosmetic(cosmetic)){
                    cosmeticButtonBox.getChildren().remove(button);
                }
            });

            cosmeticButtonBox.getChildren().add(button);
        }
    }

    // Reads through dailyTasks.csv and stores the tasks in an arraylist
    private void getDailies(){
        String resourcePath = "/edu/utsa/cs3443/chrono/files/dailyTasks.csv";

        try (InputStream is = getClass().getResourceAsStream(resourcePath);
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {

            if (is == null) {
                System.err.println("CSV file not found: " + resourcePath);
                return;
            }

            // Read the ENTIRE single line (since you have only one line with 6 tasks)
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tasks = line.split(",");  // Now safe: split only on top-level commas

                for (String task : tasks) {
                    String trimmed = task.trim();
                    if (!trimmed.isEmpty()) {
                        dailyTasks.add(trimmed);
                    }
                }
            }

            System.out.println("Successfully loaded " + dailyTasks.size() + " daily tasks:");
            dailyTasks.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error reading dailyTasks.csv");
            e.printStackTrace();
        }
    }

    // Sets the three daily tasks to random tasks in the arraylist
    private void setDailies(Button task1, Button task2, Button task3){
        Random random = new Random();
        Set<Integer> usedIndexes = new HashSet<>();
        Button[] buttons = {task1, task2, task3};

        for (int i = 0; i < 3; i++) {
            int index;
            do {
                index = random.nextInt(dailyTasks.size());
            } while (usedIndexes.contains(index)); // ensure no repeat

            usedIndexes.add(index);
            buttons[i].setText(dailyTasks.get(index));
        }
    }

    private static final String PROGRESS_FILE = "dailyProgress.txt";
    private LocalDate lastResetDate = null;

    private void loadOrResetDailyProgress(){
        File file = getProgressFile();
        Properties props = new Properties();

        try {
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    props.load(fis);
                }

                String lastDateStr = props.getProperty("lastReset");
                if (lastDateStr != null) {
                    lastResetDate = LocalDate.parse(lastDateStr);
                }
            }
        } catch (Exception ignored) {
        }

        LocalDate today = LocalDate.now();

        // Check for reset
        boolean needsReset = lastResetDate == null || lastResetDate.equals(today);

        if (needsReset) {
            System.out.println("New day detected! Resetting daily tasks...");
            resetAllTasks();
            saveDailyProgress();
            lastResetDate = today;
        } else {
            loadSavedTaskStates();
        }

    }

    private File getProgressFile(){
        // Save in home directory so it is persistent
        String userHome = System.getProperty("user.home");
        return new File(userHome + ".'/chrono" + PROGRESS_FILE);
    }

    // Variables to keep track of tasks
    private boolean task1Completed = false;
    private boolean task2Completed = false;
    private boolean task3Completed = false;

    @FXML private void toggleTask1() { toggleTask(task1, "task1Completed"); }
    @FXML private void toggleTask2() { toggleTask(task2, "task2Completed"); }
    @FXML private void toggleTask3() { toggleTask(task3, "task3Completed"); }

    private void toggleTask(Button btn, String fieldName){
        try{
            // Toggle boolean
            Field field = this.getClass().getDeclaredField(fieldName);
            boolean newValue = !field.getBoolean(this);
            field.setBoolean(this, newValue);

            // Update button color
            if(newValue){
                btn.setStyle("-fx-background-color: #00ff88; -fx-text-fill: black; -fx-font-weight: bold;");
            }else{
                btn.setStyle("-fx-background-color: #444444; -fx-text-fill: white;");
            }
            saveDailyProgress();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static final String SAVE_FILE = System.getProperty("user.home") + "./chrono/dailies.txt";
    private void saveDailyProgress(){
        Properties props = new Properties();
        props.setProperty("date", LocalDate.now().toString());
        props.setProperty("task1", String.valueOf(task1Completed));
        props.setProperty("task2", String.valueOf(task2Completed));
        props.setProperty("task3", String.valueOf(task3Completed));

        try(FileOutputStream fos = new FileOutputStream(SAVE_FILE)){
            props.store(fos, "Chrono Daily Tasks");
        }catch(Exception ignored){}
    }

    private void loadSavedTaskStates(){
        File file = new File(SAVE_FILE);
        if(!file.exists()){
            resetAllTasks();
            saveDailyProgress();
        }

        Properties props = new Properties();
        try(FileInputStream fis = new FileInputStream(file)){
            props.load(fis);
        }catch(Exception e){
            resetAllTasks();
            saveDailyProgress();
        }

        String savedDate = props.getProperty("date", "");
        if(!LocalDate.now().toString().equals(savedDate)){
            resetAllTasks(); // New day
        }else{
            // Restore state
            task1Completed = Boolean.parseBoolean(props.getProperty("task1", "false"));
            task2Completed = Boolean.parseBoolean(props.getProperty("task2", "false"));
            task3Completed = Boolean.parseBoolean(props.getProperty("task3", "false"));
            updateAllButtonColors();
        }
        saveDailyProgress();
    }

    private void resetAllTasks(){
        task1Completed = task2Completed = task3Completed = false;
        updateAllButtonColors();
    }

    private void updateAllButtonColors(){
        updateButtonColor(task1, task1Completed);
        updateButtonColor(task2, task2Completed);
        updateButtonColor(task3, task3Completed);
    }

    private void updateButtonColor(Button btn, boolean completed){
        if(completed){
            btn.setStyle("-fx-background-color: #00ff88; -fx-text-fill: black; -fx-font-weight: bold;");
        }else{
            btn.setStyle("-fx-background-color: #444444; -fx-text-fill: white;");
        }
    }

    private void updateCoinCounter(){
        coinCounter.setText("x " + shop.getTotalCoins());
    }

    private boolean showPurchaseConfirmation(String name, int cost){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Purchase");
        confirm.setHeaderText("Do you want to purchase \"" + name + "\" for " + cost + " coins?");
        confirm.showAndWait();

        if(confirm.getResult() != ButtonType.OK)
            return false;

        if (shop.getTotalCoins() < cost) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Not Enough Coins");
            error.setHeaderText("You don't have enough coins!\n\nComplete Tasks To Earn More!");
            error.showAndWait();
            return false;
        }
        shop.updateTotalCoins(-cost);
        updateCoinCounter();
        return true;
    }

    private boolean unlockTheme(Theme theme){
        if(showPurchaseConfirmation(theme.getName(), theme.getCost())) {
            um.updateThemeUnlock(theme);
            Alert unlocked = new Alert(Alert.AlertType.INFORMATION);
            unlocked.setTitle("Theme Unlocked");
            unlocked.setHeaderText("\"" + theme.getName() + "\" Now Available In Themes Tab");
            unlocked.showAndWait();
            return true;
        }
        return false;
    }
    private boolean unlockCosmetic(Cosmetic cosmetic){
            if(showPurchaseConfirmation(cosmetic.getName(), cosmetic.getCost())) {
                um.updateCosmeticUnlock(cosmetic);
                Alert unlocked = new Alert(Alert.AlertType.INFORMATION);
                unlocked.setTitle("Cosmetic Unlocked");
                unlocked.setHeaderText("\"" + cosmetic.getName() + "\" Now Available In Creature Tab");
                unlocked.showAndWait();
                return true;
            }
            return false;
    }
}

