package com.nulfy.athora.player;

import com.nulfy.athora.objects.AthoraObject;
import com.nulfy.athora.objects.AthoraObstacle;
import com.nulfy.athora.objects.AthoraWeapon;
import com.nulfy.athora.scenes.AthoraScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    public ArrayList<AthoraWeapon> getWeapons() {
        ArrayList<AthoraWeapon> weapons = new ArrayList<>();
        for(AthoraObject item : inventory){
            if(item.getType().equals("weapon")){
                weapons.add((AthoraWeapon) item);
            }
        }
        return weapons;
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
        Iterator<AthoraWeapon> iter = getWeapons().iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraWeapon o = iter.next();
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

    public void swing(String primary, AthoraObstacle guard) {
        Iterator<AthoraWeapon> iter = getWeapons().iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraWeapon w = iter.next();
            String[] splitName = w.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase())) {
                    matched = true;
                    if(guard.isAlive()){
                        w.attack(guard, w);
                        System.out.println("You attacked the " + guard.getName() + " with a " + w.getName() + " for " + w.getDamage() + " damage.");
                        if(guard.isAlive()) System.out.println("The " + guard.getName() + " is now on " + guard.getHealth() + " HP.");
                        else System.out.println("The " + guard.getName() + " is now dead.");
                        break;
                    } else {
                        System.out.println("That " + guard.getName() + " is already dead.");
                    }
                }
            }
        }
        List<String> secondary = Arrays.asList(primary.split(" with "));
        String secondaryWord;
        if(secondary.size() < 2) secondaryWord = "nothing"; else secondaryWord = secondary.get(1);
        if(!matched) System.out.println("You can't attack a " + guard.getName() + " with a " + secondaryWord);
    }

    public AthoraObstacle findObstacle(String primary) {
        AthoraObstacle obstacle = null;
        for (AthoraObject object : currentScene.objects()) {
            String[] splitName = object.getName().split(" ");
            for (String s : splitName) {
                List<String> secondary = Arrays.asList(primary.split(" with "));
                if (secondary.get(0).contains(s.toLowerCase())) {
                    obstacle = (AthoraObstacle) object;
                    break;
                }
            }
        }
        return obstacle;
    }


}
