package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;
import com.nulfy.athora.player.AthoraPlayer;
import com.nulfy.athora.scenes.AthoraScene;
import static com.nulfy.athora.scenes.AthoraScene.currentScene;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AthoraLogic {

    static Scanner input = new Scanner(System.in);
    public static long playerHp = 10;
    public static ArrayList<String> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHp, inventory);

    public static void AwaitMovement() throws IOException, ParseException {

        AthoraScene.InitiateScenes("com/nulfy/athora/scenes/AthoraScenes.json");

        System.out.println(look());

        while(true){

            if (player.getHp() <= 0){
                System.out.println(AthoraAssets.diedMessage);
                break;
            }

            String command = input.nextLine().toLowerCase().trim();

            String[] args = command.split(" ");

            String verb = hasVerb(command);

            directionActions(command, false);

            switch (verb) {
                case "look" -> System.out.println(look());
                case "move" -> directionActions(command.replaceFirst("move", "").trim(), true);
                case "addhp" -> {
                    player.changeHp(Integer.parseInt(args[1]));
                    System.out.println(player.getHp());
                }
                case "removehp" -> {
                    player.changeHp(-Integer.parseInt(args[1]));
                    System.out.println(player.getHp());
                }
                case null -> System.out.println("I don't see a verb there");
                case default -> {
                    if(!hasDirection(command)) System.out.println("I don't understand \"" + command + "\"");
                }
            }
        }
    }

    public static String look() {
        return currentScene.getName() + "\n" + currentScene.getSetting();
    }

    public static void move(int directionIndex) {
        if((long) currentScene.getDirections().get(directionIndex).get("value") != 100) {
            currentScene = AthoraScene.athoraScenes.get(Math.toIntExact((long) currentScene.getDirections().get(directionIndex).get("value")));
            System.out.println(look());
        } else {
            if(currentScene.getDirections().get(directionIndex).get("hp") != null){
                int hpChange = (int) currentScene.getDirections().get(directionIndex).get("hp");
                player.changeHp(hpChange);
                System.out.println(currentScene.getDirectionMessage(directionIndex) + " " + hpChange);
            } else {
                System.out.println(currentScene.getDirectionMessage(directionIndex));
            }
        }
    }

    public static String hasVerb(String input){
        String[] verbs = {"restart", "quit", "go", "enter", "get", "take", "open", "move",
                "inventory", "break", "kill", "look", "addhp", "removehp"
        };
        for (int i = 0; i <= verbs.length - 1; i++) {
            if (isolatedContains(input, verbs[i])) {
                return verbs[i];
            }
        }
        return "none";
    }

    public static boolean hasDirection(String input){
        String[] directions = {"north", "east", "south", "west", "up", "down"};
        for (int i = 0; i <= directions.length - 1; i++) {
            if (isolatedContains(input, directions[i])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isolatedContains(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }

    public static void directionActions(String source, boolean move) {
        switch (source) {
            case "north" -> move(0);
            case "east" -> move(1);
            case "south" -> move(2);
            case "west" -> move(3);
            case "up" -> move(4);
            case "down" -> move(5);
            default -> {
                if(move){
                    System.out.println("Where do you want to move?");
                }
            }
        }
    }
}