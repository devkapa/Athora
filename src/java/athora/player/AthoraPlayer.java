package athora.player;

import athora.objects.*;

import java.util.*;
import java.util.stream.Collectors;

import static athora.AthoraLogic.player;
import static athora.AthoraLogic.map;

public class AthoraPlayer {

    // Define the health and inventory of the player
    int health;
    ArrayList<AthoraObject> inventory;

    // Player constructor, to be called when a new instance of the player is created
    public AthoraPlayer(int health, ArrayList<AthoraObject> inventory) {
        this.health = health;
        this.inventory = inventory;
    }

    // Return an integer value of the health of the player
    public int getHealth() {
        return health;
    }

    // Change the health of a player by the provided amount in parameters
    public void changeHealth(int amount) {
        health += amount;
    }

    // Return a resizable list array of the player's inventory
    public ArrayList<AthoraObject> getInventory() {
        return inventory;
    }

    // Return a resizable list array of all valid weapons in the player's inventory
    public ArrayList<AthoraObject> getWeapons() {
        ArrayList<AthoraObject> weapons = new ArrayList<>();
        // For each item in the inventory, if it is not food, add it to the list
        inventory.forEach(item -> {
            if (!(item instanceof AthoraFood)) {
                weapons.add(item);
            }
        });
        return weapons;
    }

    // Return a list of all AthoraObjects which match the queried String input
    public List<AthoraObject> getMatch(String args, ArrayList<AthoraObject> items) {
        // Retains all items in the array with names that contain the parameter arguments
        return items.stream().filter(item -> {
            ArrayList<String> splitArgs = new ArrayList<>(List.of(args.toLowerCase().split(" ")));
            splitArgs.retainAll(Arrays.asList(item.getName().toLowerCase().split(" ")));
            return splitArgs.size() != 0;
        }).collect(Collectors.toList());
    }

    // Returns a specific AthoraContainer that is found in the player's inventory
    public AthoraContainer getContainer(String args) {
        List<AthoraObject> match = getMatch(args, inventory);
        Optional<AthoraObject> found = match.stream().filter(item-> item instanceof AthoraContainer).findFirst();
        return (AthoraContainer) found.orElse(null);
    }

    // Add an item from the player's inventory to a container
    public void addToContents(String args, AthoraContainer container) {
        // If container does not exist
        if (container == null) {
            System.out.println("You must have the container in your inventory.");
            return;
        }
        // If no item is queried
        if (args.equals("")) {
            System.out.println("Specify what you want to add to that container.");
            return;
        }
        // Get a list of all items in the player's inventory that match the query
        List<AthoraObject> match = getMatch(args.replace(container.getName().toLowerCase(), ""), inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
            if(!(i instanceof AthoraContainer)){
                // If the container is already too heavy to fit the object, do not add it
                if ((container.getMass() + i.getMass()) <= container.getMaxMass()) {
                    // Remove from the player's inventory and add to the container's contents
                    container.getContents().add(i);
                    inventory.remove(i);
                    System.out.println("You put " + i.getName() + " into " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
                } else
                    System.out.println("The " + container.getName() + " is too heavy to fit " + i.getName());
            }
        });
    }

    // Remove an item from a container and add it to the player's inventory
    public void removeFromContents(String args, AthoraContainer container) {
        // If container does not exist
        if (container == null) {
            System.out.println("You must have the container in your inventory.");
            return;
        }
        // If no item is queried
        if (args.equals("")) {
            System.out.println("Specify what you want to remove.");
            return;
        }
        // Get a list of all items in the container that match the query
        List<AthoraObject> match = getMatch(args, container.getContents());
        if(match.size() < 1) System.out.println("That item isn't in the " + container.getName() + ".");
        match.forEach(i->{
            // If the item is found, remove it from the container and add it to the player's inventory
            if(container.getContents().contains(i)){
                container.getContents().remove(i);
                inventory.add(i);
                System.out.println("You took " + i.getName() + " out of " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
            } else {
                System.out.println("Theres no \"" + args + "\" in the " + container.getName() + ".");
            }
        });
    }

    // Pick up an item that is present in the current scene
    public void pickup(String args) {
        // If no item is queried
        if (args.equals("")) {
            System.out.println("What do you want to pick up?");
            return;
        }
        // Get a list of all items in the current scene that match the query
        List<AthoraObject> match = getMatch(args, map.getCurrentScene().getObjs());
        if(match.size() < 1) System.out.println("There is no " + args + " here.");
        match.forEach(i->{
            // If the item can be taken and is not an enemy, then add it
            // to the player's inventory and remove it from the scene
            if (i.isAccessible() && !(i instanceof AthoraEnemy)) {
                this.getInventory().add(i);
                map.getCurrentScene().getObjs().remove(i);
                System.out.println("You picked up " + i.getName());
            } else {
                // If the item cannot be picked up and is a weapon with an event, then
                // execute the event or tell the player it cannot be picked up
                if(i instanceof AthoraWeapon) ((AthoraWeapon) i).executeEvent(player);
                else System.out.println("You can't pick up a " + i.getName() + "!");
            }
        });
    }

    // Eat an item in the player's inventory
    public void eat(String args) {
        // If no item is queried
        if (args.equals("")) {
            System.out.println("Specify what you want to eat.");
            return;
        }
        // Get a list of all items in the player's inventory that match the query
        List<AthoraObject> match = getMatch(args, inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
            // If the item can be taken and is a type of food, add the saturation
            // to the player's health and remove the food from the inventory.
            if (i.isAccessible() && i instanceof AthoraFood f) {
                this.changeHealth(f.getSaturation());
                if (this.getHealth() > 10) this.health = 10;
                System.out.println("You ate " + f.getName() +
                        ". It tasted good. You gained " + f.getSaturation() + " HP." +
                        "\nYou are now on " + this.getHealth() + " HP."
                );
                inventory.remove(i);
            } else {
                System.out.println("You cannot eat a " + i.getName() + "!");
            }
        });
    }

    // Drop an item from the inventory
    public void drop(String args) {
        // If no item is queried
        if (args.equals("")) {
            System.out.println("Specify what you want to drop.");
            return;
        }
        // Get a list of all items in the player's inventory that match the query
        List<AthoraObject> match = getMatch(args, inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
            // Remove each item from the inventory and add it to the current scene
            inventory.remove(i);
            map.getCurrentScene().getObjs().add(i);
            System.out.println("Dropped " + i.getName());
        });
    }

    // Swing a weapon at an enemy, dealing it damage
    public void swing(String args, AthoraObject enemy) {
        // If the enemy does not exist
        if (enemy == null) {
            System.out.println("No enemy found.");
            return;
        }
        // If the enemy is not actually an enemy
        if (!(enemy instanceof AthoraEnemy)) {
            System.out.println("That object can't be attacked.");
            return;
        }
        // If no item is queried
        if (args.equals("")) {
            System.out.println("Specify what you want to attack.");
            return;
        }
        // Get a list of all items in the player's inventory that match the query
        List<AthoraObject> match = getMatch(args, getWeapons());
        if(match.size() < 1) System.out.println("You do not have that weapon.");
        match.forEach(w->{
            AthoraEnemy e = (AthoraEnemy) enemy;
            // If the enemy is alive, attack it and change the player's health depending on
            // the enemy's strength. If it is dead, do not attack it.
            if (e.isAlive()) {
                e.changeHealth(-w.getDamage());
                System.out.println("You attacked the " + e.getName() + " with a " + w.getName() + " for " + w.getDamage() + " damage.");
                if (e.isAlive()) {
                    System.out.println("He swings back at you, dealing " + e.getDamage() + " damage to you.\nThe " + e.getName() + " is now on " + e.getHealth() + " HP.");
                    this.changeHealth(e.getDamage());
                } else {
                    System.out.println("The " + e.getName() + " is now dead.");
                    e.setName("Dead " + e.getName());
                }
            } else {
                System.out.println("That " + e.getName() + " is already dead.");
            }
        });
    }

    // Get a specific AthoraObject from the current scene
    public AthoraObject getObj(String args) {
        List<AthoraObject> match = getMatch(args, map.getCurrentScene().getObjs());
        return match.get(0);
    }

}
