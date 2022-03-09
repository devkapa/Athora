package athora;

import athora.map.AthoraDirection;
import athora.map.AthoraMap;
import athora.objects.AthoraContainer;
import athora.objects.AthoraEnemy;
import athora.objects.AthoraObject;
import athora.player.AthoraPlayer;

import java.util.*;

public class AthoraLogic {

    // Create a scanner that reads console user input
    public static Scanner input = new Scanner(System.in);

    // Initiate the player with 10 health and an empty inventory
    public static int playerHealth = 10;
    public static ArrayList<AthoraObject> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHealth, inventory);

    public static AthoraMap map;

    public static void startGame(AthoraMap map) {

        // Set the global variable "map" to the map passed in parameters when starting the game
        AthoraLogic.map = map;

        if (map == null) {
            System.out.println("Error initiating map.");
            return;
        }

        // Print the map splash text and the setting of the initial scene
        System.out.println('\n' + map.getTitle() + '\n');

        System.out.println(look());

        // Commands to run while the player is alive
        main:
        while (player.getHealth() > 0) {

            System.out.print("> ");

            // Get input from user and identify the verb
            String command = input.nextLine().toLowerCase().trim();
            String verb = getVerb(command);

            // Get arguments given after command
            String args = command.replace(verb, "").toLowerCase().trim();

            switch (verb) {
                case "look" -> System.out.println(look()); // Print setting
                case "north", "east", "south", "west", "up", "down" -> move(verb); // Move in compass direction
                case "move", "go", "walk" -> {
                    // Move in compass direction from arguments
                    String direction = getVerb(args);
                    if (direction == null) System.out.println("Where do you want to move?");
                    else move(direction);
                }
                case "pick", "pickup", "take" -> {
                    // If player is taking item out of container
                    if (args.contains("out") || args.contains("from")) {
                        player.removeFromContents(args, player.getContainer(args));
                        break;
                    }
                    // Add item to player's inventory from args
                    player.pickup(args);
                }
                case "quit" -> {
                    // Quit the game, breaking the "main" loop and killing the player
                    break main;
                }
                case "drop", "rid" -> player.drop(args); // Remove item from player's inventory from args
                case "eat", "consume", "drink" -> player.eat(args); // Eat item from player's inventory
                case "put", "place", "insert" -> {
                    // Add an item from the player's inventory to a container
                    if (!args.contains("in")) {
                        System.out.println("Specify what you want to put that in.");
                        break;
                    }
                    player.addToContents(args, player.getContainer(args));
                }
                case "remove" -> {
                    // Remove an item from a container and add it to the player's inventory
                    if (!args.contains("out") || !args.contains("from")) {
                        System.out.println("Specify what you want to take that out of.");
                        break;
                    }
                    player.removeFromContents(args, player.getContainer(args));
                }
                case "inv", "inventory", "items", "health", "hp" -> {
                    // Create a new StringBuilder that can be appended to
                    StringBuilder inventoryString = new StringBuilder();
                    // For every item in the inventory, append its name to the StringBuilder
                    for (AthoraObject item : inventory) {
                        if (!(item instanceof AthoraContainer c))
                            inventoryString.append("\n").append("* ").append(item.getName()).trimToSize();
                        else {
                            // If the item is a container, also append the container's contents to the StringBuilder
                            inventoryString.append("\n").append("* ").append(item.getName()).append(" (to take items out type remove)").trimToSize();
                            for (AthoraObject o : c.getContents()) {
                                inventoryString.append("\n   - ").append(o.getName());
                            }
                        }
                    }
                    // Append (none) to the StringBuilder if the inventory is empty
                    if (inventoryString.isEmpty()) inventoryString.append("(none)");
                    // Print the StringBuilder of the inventory and the player's health
                    System.out.println("Inventory: " + inventoryString + "\nHealth: " + player.getHealth());
                }
                case "kill", "attack", "knife", "stab", "hit", "murder" -> {
                    // Attack an enemy in the scene with a weapon in the player's inventory
                    if (!args.contains("with") || !args.contains("using")) {
                        System.out.println("Specify what you want to attack with.");
                        break;
                    }
                    player.swing(args, player.getObj(args));
                }
                default -> System.out.println("I don't understand \"" + command + "\"."); // If there is no verb, tell the user the command is not valid.
            }
        }

        // After the loop has ended (player health is less than 0) print the death message and end the game
        System.out.println(deathMessage);

    }

    public static String look() {
        // Get a list of all objects in the current scene
        List<AthoraObject> placeObjects = map.getCurrentScene().getObjs();
        // If there are no objects print a setting statement with no objects present
        if (placeObjects.isEmpty())
            return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + "There are no items here.";
        // Construct a sequence in sentence form, taking each object's name and adding it to the StringJoiner.
        StringJoiner objects = new StringJoiner(", a ", "There is a ", " here.");
        placeObjects.forEach(o->objects.add(o.getName()));
        // Then, return the setting with all objects present in a user-readable form.
        return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + objects;
    }

    public static void move(String direction) {
        // Convert the user's String input of which direction they would like to move to, and get
        // the corresponding AthoraDirection object which specifies where that direction goes.
        AthoraDirection dir = map.getCurrentScene().getDestinations().get(AthoraDirection.getIndex(direction));
        // If there is no scene corresponding to the AthoraDirection given
        if (map.findScene(dir) == null) {
            // If there is no health consequence of that direction, print the message of the direction
            if(dir.getHealth() == null) System.out.println(dir.getMessage());
            else {
                // Deduct the health consequence of the direction from the player's health and print the message of the direction
                System.out.println(dir.getMessage() + " " + dir.getHealth() + " HP");
                player.changeHealth(dir.getHealth());
            }
            return;
        }
        // For every object in the scene, if it is a living enemy that is blocking the destination scene, do not let the player move in that direction.
        if (map.getCurrentScene().getObjs().stream().anyMatch(o -> o instanceof AthoraEnemy && ((AthoraEnemy) o).getBlocking().stream().anyMatch(b -> b.equals(dir.getId())) && ((AthoraEnemy) o).isAlive())) {
            System.out.println("There is an enemy blocking your path.");
            return;
        }
        // Set the current scene to the destination scene, and print the setting of the new scene.
        map.setCurrentScene(map.findScene(dir));
        System.out.println(look());
    }

    public static String getVerb(String input) {
        // Find and return a match in the input parameter and the array of verb Strings
        // If one is not found, return "none"
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" ")));
        Optional<String> match = Arrays.stream(verbs).filter(args::contains).findFirst();
        return match.orElse("none");
    }

    // The message printed when the player has died/game is ended
    public static final String deathMessage = """
                
                **Poof! You have died.**
            Please restart the game to play again.
            """;

    // String array of verbs which can be used to make decisions and movements in the game
    public static String[] verbs = {"quit", "go", "take", "pick", "pickup", "drop", "open", "move",
            "inventory", "inv", "break", "kill", "attack", "look", "north", "east", "south", "west", "up", "down", "knife",
            "stab", "hit", "murder", "items", "walk", "rid", "eat", "consume", "drink", "hp", "health", "exit", "stop", "put",
            "place", "insert", "remove"
    };

}