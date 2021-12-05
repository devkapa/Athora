package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;
import com.nulfy.athora.objects.AthoraObject;
import com.nulfy.athora.player.AthoraPlayer;
import com.nulfy.athora.scenes.AthoraScene;
import static com.nulfy.athora.scenes.AthoraScene.currentScene;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AthoraLogic {

    static Scanner input = new Scanner(System.in);
    public static long playerHealth = 10;
    public static ArrayList<AthoraObject> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHealth, inventory);

    public static void AwaitMovement() throws IOException, ParseException {

        AthoraScene.initiateScenes("com/nulfy/athora/scenes/AthoraScenes.json", "com/nulfy/athora/objects/AthoraObjects.json");

        System.out.println(look());

        while(true){

            if (player.getHealth() <= 0){
                System.out.println(AthoraAssets.diedMessage);
                break;
            }

            String command = input.nextLine().toLowerCase().trim();
            String verb = hasVerb(command, false);

            String primary = command.replace(verb, "").trim();

            switch (verb) {
                case "look" -> System.out.println(look());
                case "north", "east", "south", "west", "up", "down" -> move(verb);
                case "move", "go", "walk" -> {
                    String direction = hasVerb(command, true);
                    if(direction == null) System.out.println("Where do you want to move?");
                    else move(direction);
                }
                case "pick", "pickup", "take" -> {
                    if (primary.equals("")) {
                        System.out.println("What do you want to pick up?");
                    } else {
                        player.pickup(primary);
                    }
                }
                case "drop" -> {
                    if (primary.equals("")) {
                        System.out.println("What do you want to drop?");
                    } else {
                        player.drop(primary);
                    }
                }
                case "inv", "inventory" -> {
                    StringBuilder inventoryString = new StringBuilder();
                    for(AthoraObject item : inventory){
                        inventoryString.append("\n").append("* ").append(item.getName()).trimToSize();
                    }
                    System.out.println("Inventory" + inventoryString);
                }
                case "kill", "attack" -> {
                    if (primary.equals("")) {
                        System.out.println("What do you want to attack?");
                        break;
                    }
                    if(primary.contains("with") || primary.contains("using")){
                        player.swing(primary, player.findObstacle(primary));
                    } else {
                        System.out.println("What do you want to attack with?");
                    }
                }
                case "none", default -> System.out.println("I don't understand \"" + command + "\".");
            }
        }
    }

    public static String look() {
        return currentScene.getName() + "\n" + currentScene.getSetting();
    }

    public static void move(String direction) {
        int directionIndex = currentScene.indexFromDirection(direction);
        if(directionIndex == 100) return;
        long directionValue = currentScene.getDirectionValue(directionIndex);
        long healthChange = currentScene.getDirectionHealthChange(directionIndex);
        if(directionValue != 100) {
            currentScene.moveTo(directionValue);
            System.out.println(look());
        } else {
            if(healthChange != 100){
                player.changeHealth(Math.toIntExact(healthChange));
                System.out.println(currentScene.getDirectionMessage(directionIndex) + " (" + Math.toIntExact(healthChange) + " HP)");
            } else {
                System.out.println(currentScene.getDirectionMessage(directionIndex));
            }
        }
    }

    public static String[] verbs = {"restart", "quit", "go", "enter", "get", "take", "pick", "pickup", "drop", "open", "move",
            "inventory", "inv", "break", "kill", "attack", "look", "north", "east", "south", "west", "up", "down", "knife"
    };

    public static String[] directions = {"north", "east", "south", "west", "up", "down"};

    public static String hasVerb(String input, boolean movement){
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" ")));
        if(movement) {
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