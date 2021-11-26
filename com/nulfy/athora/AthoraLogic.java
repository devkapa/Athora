package com.nulfy.athora;

import com.nulfy.athora.assets.AthoraAssets;
import com.nulfy.athora.player.AthoraPlayer;
import com.nulfy.athora.scenes.AthoraScene;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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

                if(command.equals("look")){
                    System.out.println(look());
                }

                if(isolatedContains(command, "move") || isolatedContains(command, "north") || isolatedContains(command, "east") || isolatedContains(command, "south") || isolatedContains(command, "west")){
                    if(isolatedContains(command, "north")) {
                        move(0);
                    } else if(isolatedContains(command, "east")) {
                        move(1);
                    } else if(isolatedContains(command, "south")) {
                        move(2);
                    } else if(isolatedContains(command, "west")) {
                        move(3);
                    } else {
                        System.out.println("Where do you want to move?");
                    }
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
        return AthoraScene.currentScene.getName() + "\n" + AthoraScene.currentScene.getSetting();
    }

    public static void move(int directionIndex) {
        if((long) AthoraScene.currentScene.getDirections().get(directionIndex).get("value") != 100) {
            AthoraScene.currentScene = AthoraScene.athoraScenes.get(Math.toIntExact((long) AthoraScene.currentScene.getDirections().get(directionIndex).get("value")));
            System.out.println(look());
        } else {
            System.out.println(AthoraScene.currentScene.getDirectionMessage(directionIndex));
        }
    }

    public static boolean hasVerb(String input){
        String[] verbs = {"north", "east", "south", "west", "restart",
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

}