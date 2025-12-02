package edu.utsa.cs3443.chrono.models;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UnlockableManager {

    private static final String themesFileName = "data/themes.csv";
    private ArrayList<Theme> themeList;
    private static final String cosmeticsFileName = "data/cosmetics.csv";
    private ArrayList<Cosmetic> cosmeticList;
    private String userActiveTheme;


//TODO maybe a get instance class
    public UnlockableManager(){

        themeList = new ArrayList<>();
        loadThemes();
        cosmeticList = new ArrayList<>();
        loadCosmetics();
        userActiveTheme = loadActiveUserTheme();
    }

    private void loadThemes(){

        Scanner scanner = null;

        try{
            String line;
            Theme theme;
            scanner = new Scanner(new File(themesFileName));

            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                theme = convertLineToTheme(line, ",");
                if(theme != null){
                    addTheme(theme);
                }
            }
        } catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        } finally{
            scanner.close();
        }

    }

    private void loadCosmetics(){
        Scanner scanner = null;

        try{
            String line;
            Cosmetic cosmetic;
            scanner = new Scanner(new File(cosmeticsFileName));

            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                cosmetic = convertLineToCosmetic(line, ",");
                if(cosmetic != null){
                    addCosmetic(cosmetic);
                }
            }
        } catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        } finally{
            scanner.close();
        }
    }

    private void addTheme(Theme theme){
        themeList.add(theme);
    }
    private void addCosmetic(Cosmetic cosmetic){
        cosmeticList.add(cosmetic);
    }

    private Theme convertLineToTheme(String line, String delimeter){

        String[] fields = line.split(delimeter);

        if(fields.length != 5){
            return null;
        }

        return new Theme(Integer.parseInt(fields[0]),Boolean.parseBoolean(fields[1]),fields[2],fields[3],fields[4]);
    }

    private Cosmetic convertLineToCosmetic(String line, String delimeter){
        String[] fields = line.split(delimeter);

        if(fields.length != 5){
            return null;
        }

        return new Cosmetic(Integer.parseInt(fields[0]),Boolean.parseBoolean(fields[1]),fields[2],Integer.parseInt(fields[3]),fields[4]);
    }

    private void saveThemeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(themesFileName))) {
            for (Theme theme : themeList) {
                bw.write(themeToLine(theme));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private void saveCosmeticToFile(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(cosmeticsFileName))) {
            for (Cosmetic cosmetic : cosmeticList) {
                bw.write(cosmeticToLine(cosmetic));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private String themeToLine(Theme theme){
        return theme.getCost() + "," + theme.isUnlocked() + "," + theme.getName() + "," + theme.getThemeCSS() + "," + theme.getButtonTheme();
    }

    private String cosmeticToLine(Cosmetic cosmetic){
        return cosmetic.getCost() + "," + cosmetic.isUnlocked() + "," + cosmetic.getName() + "," + cosmetic.getType() + "," + cosmetic.getImageFilePath();
    }

    public void updateCosmeticUnlock(Cosmetic cosmetic){
        cosmetic.setUnlocked(true);
        saveCosmeticToFile();
    }

    public void updateThemeUnlock(Theme theme){
        theme.setUnlocked(true);
        saveThemeToFile();
    }

    private void saveUserActiveTheme(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/userActiveTheme.txt"))){
            bw.write(userActiveTheme);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String loadActiveUserTheme(){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader("data/userActiveTheme.txt"))){
            line = br.readLine();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return line;
    }

    public ArrayList<Theme> getThemeList() {
        return themeList;
    }

    public void setThemeList(ArrayList<Theme> themeList) {
        this.themeList = themeList;
    }

    public String getThemesFileName() {
        return themesFileName;
    }


    public String getCosmeticsFileName() {
        return cosmeticsFileName;
    }


    public ArrayList<Cosmetic> getCosmeticList() {
        return cosmeticList;
    }

    public void setCosmeticList(ArrayList<Cosmetic> cosmeticList) {
        this.cosmeticList = cosmeticList;
    }

    public String getUserActiveTheme() {
        return userActiveTheme;
    }

    public void setUserActiveTheme(String userActiveTheme) {
        this.userActiveTheme = userActiveTheme;
        saveUserActiveTheme();
    }
}
