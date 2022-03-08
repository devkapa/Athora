package athora.player;

import athora.objects.*;

import java.util.*;
import java.util.stream.Collectors;

import static athora.AthoraLogic.player;
import static athora.AthoraLogic.map;

public class AthoraPlayer {

    int health;
    ArrayList<AthoraObject> inventory;

    public AthoraPlayer(int health, ArrayList<AthoraObject> inventory) {
        this.health = health;
        this.inventory = inventory;
    }

    public int getHealth() {
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
        inventory.forEach(item -> {
            if (!(item instanceof AthoraFood)) {
                weapons.add(item);
            }
        });
        return weapons;
    }

    public List<AthoraObject> getMatch(String args, ArrayList<AthoraObject> items) {
        return items.stream().filter(item -> {
            ArrayList<String> splitArgs = new ArrayList<>(List.of(args.toLowerCase().split(" ")));
            splitArgs.retainAll(Arrays.asList(item.getName().toLowerCase().split(" ")));
            return splitArgs.size() != 0;
        }).collect(Collectors.toList());
    }

    public AthoraContainer getContainer(String args) {
        List<AthoraObject> match = getMatch(args, inventory);
        Optional<AthoraObject> found = match.stream().filter(item-> item instanceof AthoraContainer).findFirst();
        return (AthoraContainer) found.orElse(null);
    }

    public void addToContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println("You must have the container in your inventory.");
            return;
        }
        if (args.equals("")) {
            System.out.println("Specify what you want to put.");
            return;
        }
        List<AthoraObject> match = getMatch(args, inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
            if(!(i instanceof AthoraContainer)){
                if ((container.getMass() + i.getMass()) <= container.getMaxMass()) {
                    container.getContents().add(i);
                    inventory.remove(i);
                    System.out.println("You put " + i.getName() + " into " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
                } else
                    System.out.println("The " + container.getName() + " is too heavy to fit " + i.getName());
            }
        });
    }

    public void removeFromContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println("You must have the container in your inventory.");
            return;
        }
        if (args.equals("")) {
            System.out.println("Specify what you want to remove.");
            return;
        }
        List<AthoraObject> match = getMatch(args.toLowerCase().replace(container.getName().toLowerCase(), ""), container.getContents());
        if(match.size() < 1) System.out.println("That item isn't in the " + container.getName() + ".");
        match.forEach(i->{
            if(container.getContents().contains(i)){
                container.getContents().remove(i);
                inventory.add(i);
                System.out.println("You took " + i.getName() + " out of " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
            } else {
                System.out.println("Theres no \"" + args + "\" in the " + container.getName() + ".");
            }
        });
    }

    public void pickup(String args) {
        if (args.equals("")) {
            System.out.println("What do you want to pick up?");
            return;
        }
        List<AthoraObject> match = getMatch(args, map.getCurrentScene().getObjs());
        if(match.size() < 1) System.out.println("There is no " + args + " here.");
        match.forEach(i->{
            if (i.isAccessible() && !(i instanceof AthoraEnemy)) {
                this.getInventory().add(i);
                map.getCurrentScene().getObjs().remove(i);
                System.out.println("You picked up " + i.getName());
            } else {
                if(i instanceof AthoraWeapon) ((AthoraWeapon) i).executeEvent(player);
                else System.out.println("You can't pick up a " + i.getName() + "!");
            }
        });
    }

    public void eat(String args) {
        if (args.equals("")) {
            System.out.println("Specify what you want to eat.");
            return;
        }
        List<AthoraObject> match = getMatch(args, inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
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

    public void drop(String args) {
        if (args.equals("")) {
            System.out.println("Specify what you want to drop.");
            return;
        }
        List<AthoraObject> match = getMatch(args, inventory);
        if(match.size() < 1) System.out.println("You do not have that.");
        match.forEach(i->{
            inventory.remove(i);
            map.getCurrentScene().getObjs().add(i);
            System.out.println("Dropped " + i.getName());
        });
    }

    public void swing(String args, AthoraObject enemy) {
        if (enemy == null) {
            System.out.println("No enemy found.");
            return;
        } else if (!(enemy instanceof AthoraEnemy)) {
            System.out.println("That object can't be attacked.");
            return;
        }
        if (args.equals("")) {
            System.out.println("Specify what you want to attack.");
            return;
        }
        List<AthoraObject> match = getMatch(args, getWeapons());
        if(match.size() < 1) System.out.println("You do not have that weapon.");
        match.forEach(i->{
            AthoraEnemy e = (AthoraEnemy) enemy;
            if (e.isAlive()) {
                i.attack(e, i);
                System.out.println("You attacked the " + e.getName() + " with a " + i.getName() + " for " + i.getDamage() + " damage.");
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

    public AthoraObject getObj(String args) {
        List<AthoraObject> match = getMatch(args, map.getCurrentScene().getObjs());
        return match.get(0);
    }

}
