package athora.assets;

public class AthoraAssets {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String diedMessage = ANSI_RED + "\n\n    **Poof! You have died.**   "+ ANSI_WHITE + "\nPlease restart the game to play again.\n" + ANSI_RESET;

    public static String[] verbs = {"quit", "go", "take", "pick", "pickup", "drop", "open", "move",
            "inventory", "inv", "break", "kill", "attack", "look", "north", "east", "south", "west", "up", "down", "knife",
            "stab", "hit", "murder", "items", "walk", "rid", "eat", "consume", "drink", "hp", "health", "exit", "stop", "put",
            "place", "insert", "remove"
    };
}
