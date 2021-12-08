package com.nulfy.athora.player;

import com.nulfy.athora.objects.*;

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

    public ArrayList<AthoraObject> getWeapons() {
        ArrayList<AthoraObject> weapons = new ArrayList<>();
        for(AthoraObject item : inventory){
            if(item.getType().equals("weapon") || item.getType().equals("container") || item.getType().equals("item")){
                weapons.add(item);
            }
        }
        return weapons;
    }

    public AthoraContainer getContainer(String primary) {
        AthoraContainer container = null;
        for (AthoraObject o : inventory) {
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                List<String> secondary = Arrays.asList(primary.split(" "));
                if (secondary.contains(s.toLowerCase())) {
                    if(o.getType().equals("container")) {
                        container = (AthoraContainer) o;
                        break;
                    }
                }
            }
        }
        return container;
    }

    public void addToContents(String primary, AthoraContainer container){
        if(container == null){
            System.out.println("You must have the container in your inventory.");
            return;
        }
        Iterator<AthoraObject> iter = inventory.iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject o = iter.next();
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase()) && o != container) {
                    matched = true;
                    if(o.getMass() + container.getMass() < container.getMaxMass()) {
                        container.getContents().add(o);
                        iter.remove();
                        System.out.println("You put " + o.getName() + " into " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
                    }
                    else System.out.println(o.getName() + " can't fit in the " + container.getName() + " because it is too heavy.");
                }
            }
        }
        if(!matched) System.out.println("You don't have a \"" + primary + "\".");
    }

    public void removeFromContents(String primary, AthoraContainer container){
        if(container == null){
            System.out.println("You must have the container in your inventory.");
            return;
        }
        Iterator<AthoraObject> iter = container.getContents().iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject o = iter.next();
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase()) && o != container) {
                    matched = true;
                    iter.remove();
                    inventory.add(o);
                    System.out.println("You took " + o.getName() + " out of " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
                }
            }
        }
        if(!matched) System.out.println("You don't have a \"" + primary + "\".");
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
                    if (o.isAccessible() && o.getType().equals("item") || o.isAccessible() && o.getType().equals("weapon") || o.isAccessible() && o.getType().equals("container") || o.isAccessible() && o.getType().equals("food")) {
                        this.getInventory().add(o);
                        iter.remove();
                        System.out.println("You picked up " + o.getName());
                        break;
                    } else {
                        if(o.getType().equals("weapon")) {
                            AthoraWeapon w = (AthoraWeapon) o;
                            w.executeEvent(this);
                        }
                        else System.out.println("You cannot pick up a " + o.getName() + "!");
                    }
                }
            }
        }
        if(!matched) System.out.println("Theres no \"" + primary + "\" here.");
    }

    public void eat(String primary) {
        Iterator<AthoraObject> iter = inventory.iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject o = iter.next();
            String[] splitName = o.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase())) {
                    matched = true;
                    if (o.isAccessible() && o.getType().equals("food")) {
                        AthoraFood f = (AthoraFood) o;
                        this.changeHealth((int) f.getSaturation());
                        if(this.getHealth() > 10) this.health = 10;
                        System.out.println("You ate " + f.getName() +
                                ". It tasted good. You gained " + f.getSaturation() + " HP." +
                                "\nYou are now on " + this.getHealth() + " HP."
                        );
                        iter.remove();
                        break;
                    } else {
                        System.out.println("You cannot eat a " + o.getName() + "!");
                    }
                }
            }
        }
        if(!matched) System.out.println("You don't have a \"" + primary + "\".");
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

    public void swing(String primary, AthoraObstacle guard) {
        if(guard == null){
            System.out.println("There is either no enemy here or that object can't be attacked.");
            return;
        }
        Iterator<AthoraObject> iter = getWeapons().iterator();
        boolean matched = false;
        while (iter.hasNext()) {
            AthoraObject w = iter.next();
            String[] splitName = w.getName().split(" ");
            for (String s : splitName) {
                if (Arrays.asList(primary.split(" ")).contains(s.toLowerCase())) {
                    matched = true;
                    if(guard.isAlive()){
                        w.attack(guard, w);
                        System.out.println("You attacked the " + guard.getName() + " with a " + w.getName() + " for " + w.getDamage() + " damage.");
                        if(guard.isAlive()) {
                            System.out.println("He swings back at you, dealing " + guard.getDamage() + " damage to you.\nThe " + guard.getName() + " is now on " + guard.getHealth() + " HP.");
                            this.changeHealth((int) guard.getDamage());
                        } else {
                            System.out.println("The " + guard.getName() + " is now dead.");
                        }
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
                List<String> secondary = Arrays.asList(primary.split(" "));
                if (secondary.get(0).contains(s.toLowerCase())) {
                    if(object.getType().equals("obstacle")) {
                        obstacle = (AthoraObstacle) object;
                        break;
                    }
                }
            }
        }
        return obstacle;
    }


}
