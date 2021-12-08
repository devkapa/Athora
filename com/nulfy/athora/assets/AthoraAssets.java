package com.nulfy.athora.assets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AthoraAssets {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String diedMessage = ANSI_RED + "\n\n    **Poof! You have died.**   "+ ANSI_WHITE + "\nPlease restart the game to play again.\n";

    public static String str;

    public static String[] verbs = {"quit", "go", "enter", "get", "take", "pick", "pickup", "drop", "open", "move",
            "inventory", "inv", "break", "kill", "attack", "look", "north", "east", "south", "west", "up", "down", "knife",
            "stab", "hit", "murder", "items", "walk", "rid", "eat", "consume", "drink", "hp", "health", "exit", "stop", "put",
            "place", "insert", "remove"
    };

    public static String[] directions = {"north", "east", "south", "west", "up", "down"};

    static {
        try {
            str = Files.readString(Paths.get("com/nulfy/athora/assets/AthoraLogo.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String welcome = "Hello. Welcome to " +
            "\n\n" +
            str +
            "\n\n" +
            "The year is 2137. Renowned billionaire Felon Muks, CEO of Tesca, The Interesting Company and SpaceF, has mutilated the structure of society." +
            "\n\n" +
            "With slavery rates skyrocketing, humanity prays in fear as the world crumbles beneath his evil fist..." +
            "\n";

}
