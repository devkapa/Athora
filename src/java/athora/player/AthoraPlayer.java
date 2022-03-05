package athora.player;

import athora.objects.*;

import java.util.*;
import java.util.stream.Collectors;

import static athora.AthoraLogic.player;
import static athora.assets.AthoraAssets.ANSI_RESET;
import static athora.AthoraLogic.map;

public class AthoraPlayer {

    int health;
    ArrayList<AthoraInvItem> inventory;

    public AthoraPlayer(int health, ArrayList<AthoraInvItem> inventory) {
        this.health = health;
        this.inventory = inventory;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
    }

    public ArrayList<AthoraInvItem> getInventory() {
        return inventory;
    }

    public ArrayList<AthoraInvItem> getWeapons() {
        ArrayList<AthoraInvItem> weapons = new ArrayList<>();
        inventory.forEach(item -> {
            if (item instanceof AthoraWeapon || item instanceof AthoraContainer || item instanceof AthoraObject) {
                weapons.add(item);
            }
        });
        return weapons;
    }

    public List<AthoraInvItem> getMatch(String args, ArrayList<AthoraInvItem> items) {
        return items.stream().filter(item -> {
            ArrayList<String> splitArgs = new ArrayList<>(List.of(args.toLowerCase().split(" ")));
            splitArgs.retainAll(Arrays.asList(item.getName().toLowerCase().split(" ")));
            return splitArgs.size() != 0;
        }).collect(Collectors.toList());
    }

    public AthoraContainer getContainer(String args) {
        List<AthoraInvItem> match = getMatch(args, inventory);
        Optional<AthoraInvItem> found = match.stream().filter(item-> item instanceof AthoraContainer).findFirst();
        return (AthoraContainer) found.orElse(null);
    }

    public void addToContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println(ANSI_RESET + "You must have the container in your inventory.");
            return;
        }
        List<AthoraInvItem> match = getMatch(args, inventory);
        match.forEach(i->{
            if(!(i instanceof AthoraContainer)){
                if ((container.getMass() + i.getMass()) <= container.getMaxMass()) {
                    container.getContents().add(i);
                    inventory.remove(i);
                    System.out.println(ANSI_RESET + "You put " + i.getName() + " into " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
                } else
                    System.out.println(ANSI_RESET + "The " + container.getName() + " is too heavy to fit " + i.getName());
            }
        });
    }

    public void removeFromContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println(ANSI_RESET + "You must have the container in your inventory.");
            return;
        }
        List<AthoraInvItem> match = getMatch(args.toLowerCase().replace(container.getName().toLowerCase(), ""), inventory);
        match.forEach(i->{
            if(container.getContents().contains(i)){
                container.getContents().remove(i);
                inventory.add(i);
                System.out.println(ANSI_RESET + "You took " + i.getName() + " out of " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
            } else {
                System.out.println(ANSI_RESET + "Theres no \"" + args + "\" in the " + container.getName() + ".");
            }
        });
    }

    public void pickup(String args) {
        List<AthoraInvItem> match = getMatch(args, map.getCurrentScene().getObjs());
        match.forEach(i->{
            if (i.isAccessible() && !(i instanceof AthoraEnemy)) {
                this.getInventory().add(i);
                map.getCurrentScene().getObjs().remove(i);
                System.out.println(ANSI_RESET + "You picked up " + i.getName());
            } else {
                if(i instanceof AthoraWeapon) ((AthoraWeapon) i).executeEvent(player);
                else System.out.println(ANSI_RESET + "You can't pick up a " + i.getName() + "!");
            }
        });
    }

    public void eat(String args) {
        List<AthoraInvItem> match = getMatch(args, inventory);
        match.forEach(i->{
            if (i.isAccessible() && i instanceof AthoraFood f) {
                this.changeHealth(f.getSaturation());
                if (this.getHealth() > 10) this.health = 10;
                System.out.println(ANSI_RESET + "You ate " + f.getName() +
                        ". It tasted good. You gained " + f.getSaturation() + " HP." +
                        "\nYou are now on " + this.getHealth() + " HP."
                );
                inventory.remove(i);
            } else {
                System.out.println(ANSI_RESET + "You cannot eat a " + i.getName() + "!");
            }
        });
    }

    public void drop(String args) {
        List<AthoraInvItem> match = getMatch(args, inventory);
        match.forEach(i->{
            inventory.remove(i);
            map.getCurrentScene().getObjs().add(i);
            System.out.println(ANSI_RESET + "Dropped " + i.getName());
        });
    }

    public void swing(String args, AthoraInvItem enemy) {
        if (enemy == null) {
            System.out.println(ANSI_RESET + "No enemy found.");
            return;
        } else if (!(enemy instanceof AthoraEnemy)) {
            System.out.println(ANSI_RESET + "That object can't be attacked.");
            return;
        }
        List<AthoraInvItem> match = getMatch(args, getWeapons());
        match.forEach(i->{
            AthoraEnemy e = (AthoraEnemy) enemy;
            if (e.isAlive()) {
                i.attack(e, i);
                System.out.println(ANSI_RESET + "You attacked the " + e.getName() + " with a " + i.getName() + " for " + i.getDamage() + " damage.");
                if (e.isAlive()) {
                    System.out.println(ANSI_RESET + "He swings back at you, dealing " + e.getDamage() + " damage to you.\nThe " + e.getName() + " is now on " + e.getHealth() + " HP.");
                    this.changeHealth(e.getDamage());
                } else {
                    System.out.println(ANSI_RESET + "The " + e.getName() + " is now dead.");
                    e.setName("Dead " + e.getName());
                }
            } else {
                System.out.println(ANSI_RESET + "That " + e.getName() + " is already dead.");
            }
        });
    }

    public AthoraInvItem getObj(String args) {
        List<AthoraInvItem> match = getMatch(args, map.getCurrentScene().getObjs());
        return match.get(0);
    }

}
