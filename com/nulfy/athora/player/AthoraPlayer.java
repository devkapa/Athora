package com.nulfy.athora.player;

import com.nulfy.athora.objects.AthoraObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.nulfy.athora.scenes.AthoraScene.currentScene;

public class AthoraPlayer {

    long health;
    ArrayList<AthoraObject> inventory;

    public AthoraPlayer(long health, ArrayList<AthoraObject> inventory) {
        this.health = health;
        this.inventory = inventory;
    }

    public long getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
    }

    public ArrayList<AthoraObject> getInventory() {
        return inventory;
    }

    public void pickup(String primary) {
        Iterator<AthoraObject> iter = currentScene.objects().iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject o = iter.next();
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase())) {
                    matched = true;
                    if (o.isAccessible() && o.getType().equals("item") || o.getType().equals("weapon")) {
                        this.getInventory().add(o);
                        iter.remove();
                        System.out.println("You picked up " + o.getName());
                        break;
                    } else {
                        System.out.println("You cannot pick up a " + o.getName() + "!");
                    }
                }
            }
        }
        if(!matched) System.out.println("Theres no \"" + primary + "\" here.");
    }

    public void drop(String primary) {
        Iterator<AthoraObject> iter = inventory.iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject o = iter.next();
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase())) {
                    matched = true;
                    iter.remove();
                    currentScene.objects().add(o);
                    System.out.println("Dropped " + o.getName());
                    break;
                }
            }
        }
        if(!matched) System.out.println("You don't have a \"" + primary + "\".");
    }

}
