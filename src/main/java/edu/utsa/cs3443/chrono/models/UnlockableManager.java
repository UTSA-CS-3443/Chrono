package edu.utsa.cs3443.chrono.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class UnlockableManager {

    private static final String themesFileName = "data/themes.csv";
    private ArrayList<Theme> themeList;
    private static final String cosmeticsFileName = "data/cosmetics.csv";
    private ArrayList<Cosmetic> cosmeticList;

    public UnlockableManager(){

        themeList = new ArrayList<>();
        loadThemes();
        cosmeticList = new ArrayList<>();
        loadCosmetics();

    }

    public void loadCosmetics(){
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

    public Cosmetic convertLineToCosmetic(String line, String delimeter){
        String[] fields = line.split(delimeter);

        if(fields.length != 5){
            return null;
        }

        return new Cosmetic(Integer.parseInt(fields[0]),Boolean.parseBoolean(fields[1]),fields[2],Integer.parseInt(fields[3]),fields[4]);
    }


    public void loadThemes(){

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

    public void addTheme(Theme theme){
        themeList.add(theme);
    }
    public void addCosmetic(Cosmetic cosmetic){
        cosmeticList.add(cosmetic);
    }

    public Theme convertLineToTheme(String line, String delimeter){

        String[] fields = line.split(delimeter);

        if(fields.length != 5){
            return null;
        }

        return new Theme(Integer.parseInt(fields[0]),Boolean.parseBoolean(fields[1]),fields[2],fields[3],fields[4]);
    }

    public void updateThemeUnlock(Theme theme){

        //TODO rewrite whole file with updated value, but organize array list so unlocked themes are always at the top
        try {
            theme.setUnlocked(true);
            saveDataToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String themeToLine(Theme theme){
        return theme.getCost() + "," + theme.isUnlocked() + "," + theme.getName() + "," + theme.getThemeCSS() + "," + theme.getButtonTheme();
    }

    public void saveDataToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(themesFileName))) {
            for (Theme theme : themeList) {
                bw.write(themeToLine(theme));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
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
}
