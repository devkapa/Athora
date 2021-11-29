package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;
import com.nulfy.athora.player.AthoraPlayer;
import com.nulfy.athora.scenes.AthoraScene;
import static com.nulfy.athora.scenes.AthoraScene.currentScene;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AthoraLogic {

    static Scanner input = new Scanner(System.in);

    public static void AwaitMovement() throws IOException, ParseException {

        AthoraScene.InitiateScenes("com/nulfy/athora/scenes/AthoraScenes.json");

        long playerHp = 10;
        ArrayList<String> inventory = new ArrayList<>();

        AthoraPlayer player = new AthoraPlayer(playerHp, inventory);

        System.out.println(look());

        while(true){

            if (player.getHp() <= 0){
                System.out.println(AthoraAssets.diedMessage);
                break;
            }

            String command = input.nextLine().toLowerCase().trim();

            String[] args = command.split(" ");

            if(hasVerb(command)) {

                directionActions(command, false);

                if(isolatedContains(command, "look")){
                    System.out.println(look());
                }

                if(isolatedContains(command, "move")){
                    directionActions(command.replaceFirst("move", "").trim(), true);
                }

                if(isolatedContains(command, "addhp")){
                    player.changeHp(Integer.parseInt(args[1]));
                    System.out.println(player.getHp());
                }

                if(isolatedContains(command, "removehp")){
                    player.changeHp(-Integer.parseInt(args[1]));
                    System.out.println(player.getHp());
                }
            } else {
                System.out.println("I don't know what " + command + " means.");
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
            System.out.println(currentScene.getDirectionMessage(directionIndex));
        }
    }

    public static boolean hasVerb(String input){
        String[] verbs = {"north", "east", "south", "west", "up", "down", "restart",
                "quit", "go", "enter", "get", "take", "open", "move",
                "inventory", "break", "kill", "look", "addhp", "removehp"
        };
        for (int i = 0; i <= verbs.length - 1; i++) {
            if (isolatedContains(input, verbs[i])) {
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