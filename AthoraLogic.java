import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AthoraLogic {

    static Scanner input = new Scanner(System.in);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void AwaitMovement() throws IOException, ParseException {

        AthoraScene.InitiateScenes();

        System.out.println(look());

        boolean playerAlive = true;

        while(playerAlive){
            String command = input.nextLine();

            if(hasVerb(command)) {

                if(command.equals("look")){
                    System.out.println(look());
                }

                if(command.contains("move") || command.contains("north") || command.contains("east") || command.contains("south") || command.contains("west")){

                    String[] args = command.split(" ");

                    if(args[1].equals("north") || command.contains("north")) {
                        move(0);
                    }

                    if(args[1].equals("east")) {
                        move(1);
                    }

                    if(args[1].equals("south")) {
                        move(2);
                    }

                    if(args[1].equals("west")) {
                        move(3);
                    }

                    /*
                    String digits = command.replaceAll("[A-Za-z\\s]+", "");
                    if(digits.length() > 0){
                        int res = Integer.parseInt(digits);
                        move(res);
                        System.out.println(look());
                    } else {
                        System.out.println("invalid scene number");
                    }
                     */

                }
            } else {
                System.out.println("I don't know what " + command + " means.");
            }
        }
    }

    public static String look() {
        return AthoraScene.currentScene.getSetting();
    }

    public static void move(int directionIndex) {
        AthoraScene currentScene = AthoraScene.currentScene;
        if(currentScene.getDirections().get(directionIndex) != 100) {
            AthoraScene.currentScene = AthoraScene.athoraScenes.get(Math.toIntExact(AthoraScene.currentScene.getDirections().get(directionIndex)));
            System.out.println(look());
        } else {
            System.out.println(currentScene.getDirectionMessage(directionIndex));
        }
    }

    public static boolean hasVerb(String input){
        String[] verbs = {"north", "east", "south", "west", "restart",
                "quit", "go", "enter", "get", "take", "open", "move",
                "inventory", "break", "kill", "look"
        };

        for (int i = 0; i <= verbs.length - 1; i++) {
            if (input.contains(verbs[i])) {
                return true;
            }
        }
        return false;
    }

}