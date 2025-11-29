package edu.utsa.cs3443.chrono.models;

public class Cosmetic extends Unlockable{

    private String name;
    private String imageFilePath;
    private int type; //0 for hats, 1 for clothes, 2 for items, etc.


    public Cosmetic(int cost, boolean unlocked, String name, int type, String imageFilePath){
        super(cost, unlocked);
        this.name = name;
        this.type = type;
        this.imageFilePath = imageFilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
