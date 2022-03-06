package athora;

import athora.assets.AthoraAssets;
import athora.map.AthoraDirection;
import athora.map.AthoraMap;
import athora.map.AthoraScene;
import athora.objects.AthoraContainer;
import athora.objects.AthoraEnemy;
import athora.objects.AthoraInvItem;
import athora.player.AthoraPlayer;

import java.io.InputStream;
import java.util.*;

import static athora.assets.AthoraAssets.*;

public class AthoraLogic {

    public static Scanner input = new Scanner(System.in);
    public static int playerHealth = 10;
    public static ArrayList<AthoraInvItem> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHealth, inventory);

    public static AthoraMap map;

    public static void startGame(AthoraMap map) {

        AthoraLogic.map = map;

        if (map == null) {
            System.out.println("Error initiating map.");
            return;
        }

        System.out.println('\n' + map.getTitle() + '\n');

        System.out.println(ANSI_RESET + look(map));

        main:
        while (true) {

            if (player.getHealth() <= 0) {
                System.out.println(ANSI_RESET + AthoraAssets.diedMessage);
                break;
            }

            System.out.print(ANSI_RESET + "> " + ANSI_GREEN);
            String command = input.nextLine().toLowerCase().trim();
            String verb = getVerb(command);

            String args = command.replace(verb, "").toLowerCase().trim();

            switch (verb) {
                case "look" -> System.out.println(ANSI_RESET + look(map));
                case "north", "east", "south", "west", "up", "down" -> move(map, verb);
                case "move", "go", "walk" -> {
                    String direction = getVerb(args);
                    if (direction == null) System.out.println(ANSI_RESET + "Where do you want to move?");
                    else move(map, direction);
                }
                case "pick", "pickup", "take" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "What do you want to pick up?");
                    } else {
                        player.pickup(args);
                    }
                }
                case "exit", "stop" -> {
                    break main;
                }
                case "drop", "rid" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "Specify what you want to drop.");
                    } else {
                        player.drop(args);
                    }
                }
                case "eat", "consume", "drink" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "Specify what you want to eat.");
                    } else {
                        player.eat(args);
                    }
                }
                case "put", "place", "insert" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "Specify what you want to put.");
                        break;
                    }
                    if (args.contains("in")) {
                        player.addToContents(args, player.getContainer(args));
                    } else {
                        System.out.println(ANSI_RESET + "Specify what you want to put that in.");
                    }
                }
                case "remove" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "Specify what you want to remove.");
                        break;
                    }
                    if (args.contains("out") || args.contains("from")) {
                        player.removeFromContents(args, player.getContainer(args));
                    } else {
                        System.out.println(ANSI_RESET + "Specify what you want to take that out of.");
                    }
                }
                case "inv", "inventory", "items", "health", "hp" -> {
                    StringBuilder inventoryString = new StringBuilder();
                    for (AthoraInvItem item : inventory) {
                        if (!(item instanceof AthoraContainer c))
                            inventoryString.append("\n").append("* ").append(item.getName()).trimToSize();
                        else {
                            inventoryString.append("\n").append("* ").append(item.getName()).append(" (to take items out type remove)").trimToSize();
                            for (AthoraInvItem o : c.getContents()) {
                                inventoryString.append("\n   - ").append(o.getName());
                            }
                        }
                    }
                    if (inventoryString.isEmpty()) inventoryString.append("(none)");
                    System.out.println(ANSI_RESET + "Inventory: " + inventoryString + "\nHealth: " + player.getHealth());
                }
                case "kill", "attack", "knife", "stab", "hit", "murder" -> {
                    if (args.equals("")) {
                        System.out.println(ANSI_RESET + "Specify what you want to attack.");
                        break;
                    }
                    if (args.contains("with") || args.contains("using")) {
                        player.swing(args, player.getObj(args));
                    } else {
                        System.out.println(ANSI_RESET + "Specify what you want to attack with.");
                    }
                }
                default -> System.out.println(ANSI_RESET + "I don't understand \"" + command + "\".");
            }
        }
    }

    public static String look(AthoraMap map) {
        List<AthoraInvItem> placeObjects = map.getCurrentScene().getObjs();
        if (placeObjects.isEmpty())
            return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + "There are no items here.";
        StringJoiner objects = new StringJoiner(", a ", "There is a ", " here.");
        placeObjects.forEach(o->objects.add(o.getName()));
        return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + objects;
    }

    public static void move(AthoraMap map, String direction) {
        AthoraDirection dir = map.getCurrentScene().getDestinations().get(AthoraDirection.getIndex(direction));
        if (map.findScene(dir) == null) {
            if(dir.getHealth() == null) System.out.println(ANSI_RESET + dir.getMessage());
            else {
                System.out.println(ANSI_RESET + dir.getMessage() + " " + dir.getHealth() + " HP");
                player.changeHealth(dir.getHealth());
            }
            return;
        }
        if (map.getCurrentScene().getObjs().stream().anyMatch(o -> o instanceof AthoraEnemy && ((AthoraEnemy) o).getBlocking().stream().anyMatch(b -> b.equals(dir.getId())) && ((AthoraEnemy) o).isAlive())) {
            System.out.println("There is an enemy blocking your path.");
            return;
        }
        map.setCurrentScene(map.findScene(dir));
        System.out.println(ANSI_RESET + look(map));
    }

    public static String getVerb(String input) {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" ")));
        Optional<String> match = Arrays.stream(verbs).filter(args::contains).findFirst();
        return match.orElse("none");
    }
}