package edu.utsa.cs3443.chrono.models;

import java.io.*;

public class ShopModel {
    private int totalCoins;

    public ShopModel (){
        this.totalCoins = loadTotalCoins();
    }

    /**
     * read totalCoins.txt to get users total coins
     * @return total coins of the user
     */
    private int loadTotalCoins(){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader("data/totalCoins.txt"))){
            line = br.readLine();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return Integer.parseInt(line);
    }

    /**
     * updates the total number of coins after the user makes a purchase
     * @param updateAmount total coins to be added or removed
     */
    public void updateTotalCoins(int updateAmount){
        setTotalCoins(totalCoins + updateAmount);
        saveTotalCoinsToFile(totalCoins);
    }

    /**
     * save number of total coins the user has to totalCoins.txt
     * @param totalCoins total coins of user
     */
    private void saveTotalCoinsToFile(int totalCoins) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/totalCoins.txt"))){
            bw.write(String.valueOf(totalCoins));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }
}
