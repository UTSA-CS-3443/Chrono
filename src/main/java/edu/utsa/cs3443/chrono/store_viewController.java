package edu.utsa.cs3443.chrono;

import edu.utsa.cs3443.chrono.models.Theme;
import edu.utsa.cs3443.chrono.models.UnlockableManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
    public Button cosmetic3Button;
    @FXML
    public Button cosmetic2Button;
    @FXML
    public Button cosmetic1Button;
    @FXML
    public Button orangeThemeButton;
    @FXML
    public Button purpleThemeButton;
    @FXML
    public Button greenThemeButton;
    @FXML
    public Button redThemeButton;
    @FXML
    public Label coinCounter;
    @FXML
    private VBox dailyBackground;
    @FXML
    private Label timeLabel;
    @FXML
    private StackPane redTheme;
    @FXML
    private StackPane greenTheme;
    @FXML
    private StackPane purpleTheme;
    @FXML
    private StackPane orangeTheme;
    @FXML
    private StackPane cosmetic1;
    @FXML
    private StackPane cosmetic2;
    @FXML
    private StackPane cosmetic3;
    @FXML
    private FlowPane themeButtonBox;

    // These buttons do not do anything when clicked, they are just to show the user what needs to be done
    @FXML
    private Button task1;
    @FXML
    private Button task2;
    @FXML
    private Button task3;

    public int coins = 0;

    private final ArrayList<String> dailyTasks = new ArrayList<String>();
    private ArrayList<Theme> themeList;
    private UnlockableManager um;

    @FXML
    private void initialize(){
        getDailies();
        loadOrResetDailyProgress();
        MidnightCountdown countdown = new MidnightCountdown(timeLabel, midnightProgress);
        countdown.start();
        //loadUnlocks();
        coins = 200;
        updateCoinCounter();
        um = new UnlockableManager();
        themeList = um.getThemeList();
        createButtons();
    }

    @FXML
    public void createButtons(){

        for(Theme theme : themeList) {
            Button button = new Button(theme.getName());
            if(theme.isUnlocked()) {
                button.getStyleClass().add(theme.getButtonTheme());
            } else{
                //TODO set style to a locked version in themes.css, add locked version to theme objects
                button.setText("LOCKED - " + theme.getCost() + " Coins");
                button.setDisable(true);
            }

            button.setPrefWidth(300);
            button.setPrefHeight(50);

            button.setOnAction(e->{

            });

            themeButtonBox.getChildren().add(button);
        }
    }


    private void loadUnlocks(){
        Path path = Paths.get(unlockFilePath);

        //if file does not exist or is empty, start with 0 coins
        if(!Files.exists(path)){
            System.out.println("No save file found, starting with 0 coins.");

        }

        try{
            String content = Files.readString(path).trim();

            // if file is empty
            if(content.isEmpty()){
                System.out.println("File is empty. defaulting to 0 coins");

            }

            int savedCoins = Integer.parseInt(content);
            System.out.println("Successfully loaded " + savedCoins + " coins.");
            coins = savedCoins;
        }catch(IOException e){
            showErrorAlert("Could not read the save file");
            e.printStackTrace();
        }catch(NumberFormatException e){
            showErrorAlert("Corrupted save data");
            e.printStackTrace();
        }

        updateCoinCounter();
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
        coinCounter.setText("x " + coins);
        saveUnlocks();
    }


    private final String unlockFilePath = "/edu/utsa/cs3443/chrono/files/unlocks.txt";
    private void saveUnlocks(){
        Path path = Paths.get(unlockFilePath);

        try{
            // convert coins to string and write to file
            String coinsData = String.valueOf(coins);

            // create file directories if it cannot find it
            if(path.getParent() != null){
                Files.createDirectories(path.getParent());
            }

            // write coins value and overwrite file
            Files.writeString(path, coinsData,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Coins saved successfully: " + coins);
        }catch(IOException e){
            showErrorAlert("Failed to save unlocks and coins: could not save to file");
            e.printStackTrace();
        }
    }

    private static void showErrorAlert(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Error");
        alert.setHeaderText("Could not save");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void buyRed(ActionEvent actionEvent) {
        if(coins >= 200){
            redThemeUnlock();
            coins -= 200;
            updateCoinCounter();
        }
    }

    private void redThemeUnlock(){
        redTheme.setStyle("-fx-background-color: #9E1C1C");
        // redTheme.setStyle("-fx-background-radius: 10"); changing the radius to not make it square makes it transparent for some reason
    }

    public void buyGreen(ActionEvent actionEvent) {
        if(coins >= 200){
            greenThemeUnlock();
            coins -= 200;
            updateCoinCounter();
        }
    }

    private void greenThemeUnlock(){
        greenTheme.setStyle("-fx-background-color: #22AA2D");
    }

    public void buyPurple(ActionEvent actionEvent) {
    }

    public void buyOrange(ActionEvent actionEvent) {
    }

    public void buyCosmetic1(ActionEvent actionEvent) {
    }

    public void buyCosmetic2(ActionEvent actionEvent) {
    }

    public void buyCosmetic3(ActionEvent actionEvent) {
    }
}
