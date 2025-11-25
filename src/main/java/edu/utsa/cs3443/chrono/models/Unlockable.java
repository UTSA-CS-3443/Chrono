package edu.utsa.cs3443.chrono.models;

abstract class Unlockable {

    private int cost;
    private boolean unlocked;

    public Unlockable (int cost, boolean unlocked){
        this.cost = cost;
        this.unlocked = unlocked;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}


