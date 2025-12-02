package edu.utsa.cs3443.chrono.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ShopModel {
    private int totalCoins;

    public ShopModel (){
        this.totalCoins = loadTotalCoins();
    }

    private int loadTotalCoins(){
//TODO load users saved coins from totalCoins.txt


        return 100;
    }

    public void updateTotalCoins(int updateAmount){
        setTotalCoins(totalCoins + updateAmount);
        saveTotalCoinsToFile(totalCoins);
    }

    private void saveTotalCoinsToFile(int totalCoins){
        final String coinsFilePath = "/edu/utsa/cs3443/chrono/data/totalCoins.txt";
        Path path = Paths.get(coinsFilePath);

        try{
            // convert coins to string and write to file
            String coinsData = String.valueOf(getTotalCoins());

            // create file directories if it cannot find it
            if(path.getParent() != null){
                Files.createDirectories(path.getParent());
            }

            // write coins value and overwrite file
            Files.writeString(path, coinsData,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Coins saved successfully: " + getTotalCoins());
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
