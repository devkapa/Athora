package athora;

import athora.assets.AthoraAssets;
import athora.map.AthoraDirection;
import athora.map.AthoraMap;
import athora.map.AthoraPlace;
import athora.objects.AthoraContainer;
import athora.objects.AthoraInvItem;
import athora.objects.AthoraObstacle;
import athora.player.AthoraPlayer;
import athora.scenes.AthoraScene;

import java.io.InputStream;
import java.util.*;

import static athora.assets.AthoraAssets.*;
import static athora.scenes.AthoraScene.currentScene;

public class AthoraLogic {

    public static Scanner input = new Scanner(System.in);
    public static long playerHealth = 10;
    public static ArrayList<AthoraInvItem> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHealth, inventory);

    public static InputStream sceneFile = AthoraLogic.class.getResourceAsStream("/AthoraScenes.json");
    public static InputStream objFile = AthoraLogic.class.getResourceAsStream("/AthoraObjects.json");

    public static AthoraMap map = AthoraMap.getMap();

    public static void startGame() {

        if(map == null) {
            System.out.println("Error initiating map.");
            return;
        }

        System.out.println('\n' + map.getTitle() + '\n');

        AthoraScene.initiateScenes(sceneFile, objFile);
        System.out.println(ANSI_RESET + look());

        main: while (true) {

            if (player.getHealth() <= 0) {
                System.out.println(ANSI_RESET +AthoraAssets.diedMessage);
                break;
            }

            System.out.print(ANSI_RESET + "> " + ANSI_GREEN);
            String command = input.nextLine().toLowerCase().trim();
            String verb = hasVerb(command, false);

            String primary = command.replace(verb, "").trim();

            switch (verb) {
                case "look" -> System.out.println(ANSI_RESET + look());
                case "north", "east", "south", "west", "up", "down" -> move(verb);
                case "move", "go", "walk" -> {
                    String direction = hasVerb(command, true);
                    if (direction == null) System.out.println(ANSI_RESET +"Where do you want to move?");
                    else move(direction);
                }
                case "pick", "pickup", "take" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"What do you want to pick up?");
                    } else {
                        player.pickup(primary);
                    }
                }
                case "exit", "stop" -> {
                    break main;
                }
                case "drop", "rid" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"Specify what you want to drop.");
                    } else {
                        player.drop(primary);
                    }
                }
                case "eat", "consume", "drink" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"Specify what you want to eat.");
                    } else {
                        player.eat(primary);
                    }
                }
                case "put", "place", "insert" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"Specify what you want to put.");
                        break;
                    }
                    if (primary.contains("in")) {
                        player.addToContents(primary, player.getContainer(primary));
                    } else {
                        System.out.println(ANSI_RESET +"Specify what you want to put that in.");
                    }
                }
                case "remove" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"Specify what you want to remove.");
                        break;
                    }
                    if (primary.contains("out") || primary.contains("from")) {
                        player.removeFromContents(primary, player.getContainer(primary));
                    } else {
                        System.out.println(ANSI_RESET +"Specify what you want to take that out of.");
                    }
                }
                case "inv", "inventory", "items", "health", "hp" -> {
                    StringBuilder inventoryString = new StringBuilder();
                    for (AthoraInvItem item : inventory) {
                        if(!item.getType().equals("container"))
                        inventoryString.append("\n").append("* ").append(item.getName()).trimToSize();
                        else {
                            inventoryString.append("\n").append("* ").append(item.getName()).append(" (to take items out type remove)").trimToSize();
                            AthoraContainer c = (AthoraContainer) item;
                            for(AthoraInvItem o : c.getContents()){
                                inventoryString.append("\n   - ").append(o.getName());
                            }
                        }
                    }
                    if (inventoryString.isEmpty()) inventoryString.append("(none)");
                    System.out.println(ANSI_RESET +"Inventory: " + inventoryString + "\nHealth: " + player.getHealth());
                }
                case "kill", "attack", "knife", "stab", "hit", "murder" -> {
                    if (primary.equals("")) {
                        System.out.println(ANSI_RESET +"Specify what you want to attack.");
                        break;
                    }
                    if (primary.contains("with") || primary.contains("using")) {
                        player.swing(primary, player.findObstacle(primary));
                    } else {
                        System.out.println(ANSI_RESET +"Specify what you want to attack with.");
                    }
                }
                case "none", default -> System.out.println(ANSI_RESET +"I don't understand \"" + command + "\".");
            }
        }
    }

    public static String look() {
        List<AthoraInvItem> placeObjects = map.getCurrentPlace().getObjs();
        if (placeObjects.isEmpty())
            return map.getCurrentPlace().getName() + "\n" + map.getCurrentPlace().getSetting() + "\n" + "There are no items here.";
        StringJoiner objects = new StringJoiner(", a ", "There is a ", " here.");
        for (AthoraInvItem obj : placeObjects) {
            objects.add(obj.getName());
        }
        return map.getCurrentPlace().getName() + "\n" + map.getCurrentPlace().getSetting() + "\n" + objects;
    }

    public static void move(String direction) {
        AthoraDirection dir = AthoraDirection.getIndex(direction);
        AthoraPlace destination = map.findPlaceByCoords(dir);
        if(destination == map.getCurrentPlace() || destination == null) {
            System.out.println("You can't go there.");
            return;
        }
        map.setCurrentPlace(destination);
        System.out.println(ANSI_RESET + look());
        map.getCurrentPlace().getObjs().forEach(d -> {
            if(d.getType().equalsIgnoreCase("obstacle")){
                
            }
        });
    }

    /*
    public static void move(String direction) {
        List<AthoraInvItem> sceneObjects = currentScene.objects();
        int directionIndex = currentScene.indexFromDirection(direction);
        if (directionIndex == 100) return;
        long directionValue = currentScene.getDirectionValue(directionIndex);
        long healthChange = currentScene.getDirectionHealthChange(directionIndex);
        for (AthoraInvItem object : sceneObjects) {
            if (object.getType().equals("obstacle")) {
                AthoraObstacle o = (AthoraObstacle) object;
                if (o.getPositions() == null) break;
                for (Object i : o.getPositions()) {
                    long pos = (long) i;
                    if (directionIndex == pos) {
                        System.out.println(ANSI_RESET +"There is a guard there.");
                        return;
                    }
                }

            }
        }
        if (directionValue != 100) {
            currentScene.moveTo(directionValue);
            System.out.println(ANSI_RESET + look());
        } else {
            if (healthChange != 100) {
                player.changeHealth(Math.toIntExact(healthChange));
                System.out.println(ANSI_RESET +currentScene.getDirectionMessage(directionIndex) + " (" + Math.toIntExact(healthChange) + " HP)");
            } else {
                System.out.println(ANSI_RESET +currentScene.getDirectionMessage(directionIndex));
            }
        }
    }

     */



    public static String hasVerb(String input, boolean movement) {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" ")));
        if (movement) {
            args.remove(0);
            for (String direction : directions) {
                if (args.contains(direction)) return direction;
            }
        } else {
            for (String verb : verbs) {
                if (args.contains(verb)) return verb;
            }
        }
        return "none";
    }
}