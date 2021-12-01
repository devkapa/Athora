package com.nulfy.athora.player;

import com.nulfy.athora.objects.AthoraInventoryItem;
import com.nulfy.athora.scenes.AthoraScene;

import java.util.ArrayList;

public class AthoraPlayer {

    long health;
    ArrayList<AthoraInventoryItem> inventory;

    public AthoraPlayer(long health, ArrayList<AthoraInventoryItem> inventory) {
        this.health = health;
        this.inventory = inventory;
    }

    public long getHealth() {
        return health;
    }

    public AthoraScene getScene() { return AthoraScene.currentScene; }

    public void changeHealth(int amount){
        health += amount;
    }

    public ArrayList<AthoraInventoryItem> getInventory(){
        return inventory;
    }

}
