package athora.assets;

import athora.AthoraLogic;

import java.io.*;

public class AthoraAssets {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String diedMessage = ANSI_RED + "\n\n    **Poof! You have died.**   "+ ANSI_WHITE + "\nPlease restart the game to play again.\n" + ANSI_RESET;

    public static String str;

    static {
        try {
            str = new String(AthoraLogic.class.getResourceAsStream("/AthoraLogo.txt").readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] verbs = {"quit", "go", "take", "pick", "pickup", "drop", "open", "move",
            "inventory", "inv", "break", "kill", "attack", "look", "north", "east", "south", "west", "up", "down", "knife",
            "stab", "hit", "murder", "items", "walk", "rid", "eat", "consume", "drink", "hp", "health", "exit", "stop", "put",
            "place", "insert", "remove"
    };

    public static String[] directions = {"north", "east", "south", "west", "up", "down"};

    public static String welcome = "Hello. Welcome to " +
            "\n\n" +
            str +
            "\n\n" +
            "The year is 2137. Renowned billionaire Felon Muks, CEO of Tesca, The Interesting Company and SpaceF, has mutilated the structure of society." +
            "\n\n" +
            "With slavery rates skyrocketing, humanity prays in fear as the world crumbles beneath his evil fist..." +
            "\n";

}
