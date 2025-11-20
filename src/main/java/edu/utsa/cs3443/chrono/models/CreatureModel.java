package edu.utsa.cs3443.chrono.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Represents the state of the creature (Health, Happiness, Coins).
 * Handles persistence by reading/writing to a CSV file.
 *
 * @author Collin Schiebel
 */
public class CreatureModel {

    private final DoubleProperty health = new SimpleDoubleProperty(0.0);
    private final DoubleProperty happiness = new SimpleDoubleProperty(0.0);
    private final DoubleProperty coins = new SimpleDoubleProperty(0.0);

    private static final String DATA_FOLDER = "data";
    private static final String FILE_NAME = "creatureStats.csv";

    public CreatureModel() {
        loadStats();
    }

    public void feed() {
        double newHealth = getHealth() + 0.1;
        setHealth(Math.min(1.0, newHealth));
        saveStats();
    }

    public void water() {
        double newHappiness = getHappiness() + 0.1;
        setHappiness(Math.min(1.0, newHappiness));
        saveStats();
    }

    private void loadStats() {
        File folder = new File(DATA_FOLDER);
        File file = new File(folder, FILE_NAME);

        if (!file.exists()) {
            try {
                if (!folder.exists()) {
                    boolean dirCreated = folder.mkdirs();
                    if (!dirCreated) System.err.println("Could not create data directory.");
                }
                saveStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("health")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    setHealth(Double.parseDouble(parts[0]));
                    setHappiness(Double.parseDouble(parts[1]));
                    setCoins(Double.parseDouble(parts[2]));
                }
                break;
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading creature stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveStats() {
        File folder = new File(DATA_FOLDER);
        File file = new File(folder, FILE_NAME);

        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("health,happiness,coins");
                bw.newLine();
                bw.write(getHealth() + "," + getHappiness() + "," + getCoins());
            }
        } catch (IOException e) {
            System.err.println("Error saving creature stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getHealth() { return health.get(); }
    public void setHealth(double value) { this.health.set(value); }
    public DoubleProperty healthProperty() { return health; }

    public double getHappiness() { return happiness.get(); }
    public void setHappiness(double value) { this.happiness.set(value); }
    public DoubleProperty happinessProperty() { return happiness; }

    public double getCoins() { return coins.get(); }
    public void setCoins(double value) { this.coins.set(value); }
    public DoubleProperty coinsProperty() { return coins; }
}