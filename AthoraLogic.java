import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class AthoraLogic {

    static Scanner input = new Scanner(System.in);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void AwaitMovement() throws IOException, ParseException {

        AthoraScene.InitiateScenes();

        System.out.println(look());

        boolean playerAlive = true;

        while(playerAlive){
            String command = input.nextLine().toLowerCase();

            if(hasVerb(command)) {

                if(command.equals("look")){
                    System.out.println(look());
                }

                if(command.contains("move") || command.contains("north") || command.contains("east") || command.contains("south") || command.contains("west")){
                    if(command.contains("north")) {
                        move(0);
                    } else if(command.contains("east")) {
                        move(1);
                    } else if(command.contains("south")) {
                        move(2);
                    } else if(command.contains("west")) {
                        move(3);
                    } else {
                        System.out.println("Where do you want to move?");
                    }
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
        if(AthoraScene.currentScene.getDirections().get(directionIndex) != 100) {
            AthoraScene.currentScene = AthoraScene.athoraScenes.get(Math.toIntExact(AthoraScene.currentScene.getDirections().get(directionIndex)));
            System.out.println(look());
        } else {
            System.out.println(AthoraScene.currentScene.getDirectionMessage(directionIndex));
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