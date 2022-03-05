package athora.player;

import athora.objects.*;

import java.util.*;

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

    public Optional<AthoraInvItem> getMatch(String args, ArrayList<AthoraInvItem> items) {
        return items.stream().filter(item -> {
            ArrayList<String> splitArgs = new ArrayList<>(List.of(args.toLowerCase().split(" ")));
            splitArgs.retainAll(Arrays.asList(item.getName().toLowerCase().split(" ")));
            return splitArgs.size() != 0;
        }).findFirst();
    }

    public AthoraContainer getContainer(String args) {
        Optional<AthoraInvItem> match = getMatch(args, inventory);
        if (match.isPresent()) {
            if (match.get() instanceof AthoraContainer) {
                return (AthoraContainer) match.get();
            }
        }
        return null;
    }

    public void addToContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println(ANSI_RESET + "You must have the container in your inventory.");
            return;
        }
        Optional<AthoraInvItem> match = getMatch(args, inventory);
        if (match.isPresent()) {
            if ((container.getMass() + match.get().getMass()) < container.getMaxMass()) {
                if (match.get() instanceof AthoraContainer) {
                    System.out.println("You can't put a container in a container");
                    return;
                }
                container.getContents().add(match.get());
                inventory.remove(match.get());
                System.out.println(ANSI_RESET + "You put " + match.get().getName() + " into " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
            } else
                System.out.println(ANSI_RESET + "The " + container.getName() + " is too heavy to fit " + match.get().getName());
        } else {
            System.out.println(ANSI_RESET + "Theres no \"" + args + "\" in your inventory.");
        }
    }

    public void removeFromContents(String args, AthoraContainer container) {
        if (container == null) {
            System.out.println(ANSI_RESET + "You must have the container in your inventory.");
            return;
        }
        Optional<AthoraInvItem> match = getMatch(args, container.getContents());
        if (match.isPresent()) {
            container.getContents().remove(match.get());
            inventory.add(match.get());
            System.out.println(ANSI_RESET + "You took " + match.get().getName() + " out of " + container.getName() + ". Now it deals " + container.getDamage() + " damage.");
        } else {
            System.out.println(ANSI_RESET + "Theres no \"" + args + "\" in the " + container.getName() + ".");
        }
    }

    public void pickup(String args) {
        Optional<AthoraInvItem> match = getMatch(args, map.getCurrentScene().getObjs());
        if (match.isPresent()) {
            if (match.get().isAccessible() && !(match.get() instanceof AthoraEnemy)) {
                this.getInventory().add(match.get());
                map.getCurrentScene().getObjs().remove(match.get());
                System.out.println(ANSI_RESET + "You picked up " + match.get().getName());
            } else {
                if(match.get() instanceof AthoraWeapon) ((AthoraWeapon) match.get()).executeEvent(player);
                else System.out.println(ANSI_RESET + "You can't pick up a " + match.get().getName() + "!");
            }
        } else {
            System.out.println(ANSI_RESET + "Theres no \"" + args + "\" here.");
        }
    }

    public void eat(String args) {
        Optional<AthoraInvItem> match = getMatch(args, inventory);
        if (match.isPresent()) {
            if (match.get().isAccessible() && match.get() instanceof AthoraFood f) {
                this.changeHealth(f.getSaturation());
                if (this.getHealth() > 10) this.health = 10;
                System.out.println(ANSI_RESET + "You ate " + f.getName() +
                        ". It tasted good. You gained " + f.getSaturation() + " HP." +
                        "\nYou are now on " + this.getHealth() + " HP."
                );
                inventory.remove(match.get());
            } else {
                System.out.println(ANSI_RESET + "You cannot eat a " + match.get().getName() + "!");
            }
        } else {
            System.out.println(ANSI_RESET + "You don't have a \"" + args + "\".");
        }
    }

    public void drop(String args) {
        Optional<AthoraInvItem> match = getMatch(args, inventory);
        if (match.isPresent()) {
            inventory.remove(match.get());
            map.getCurrentScene().getObjs().add(match.get());
            System.out.println(ANSI_RESET + "Dropped " + match.get().getName());
        } else {
            System.out.println(ANSI_RESET + "You don't have a \"" + args + "\".");
        }
    }

    public void swing(String args, AthoraInvItem enemy) {
        if (enemy == null) {
            System.out.println(ANSI_RESET + "No enemy found.");
            return;
        } else if (!(enemy instanceof AthoraEnemy)) {
            System.out.println(ANSI_RESET + "That object can't be attacked.");
            return;
        }
        Optional<AthoraInvItem> match = getMatch(args, getWeapons());
        if (match.isPresent()) {
            AthoraEnemy e = (AthoraEnemy) enemy;
            if (e.isAlive()) {
                match.get().attack(e, match.get());
                System.out.println(ANSI_RESET + "You attacked the " + e.getName() + " with a " + match.get().getName() + " for " + match.get().getDamage() + " damage.");
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
        } else {
            System.out.println(ANSI_RESET + "You need a weapon to attack with.");
        }
    }

    public AthoraInvItem getObj(String args) {
        Optional<AthoraInvItem> match = getMatch(args, map.getCurrentScene().getObjs());
        return match.orElse(null);
    }

}
