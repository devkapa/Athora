package athora;

import athora.assets.AthoraAssets;
import athora.map.AthoraDirection;
import athora.map.AthoraMap;
import athora.objects.AthoraContainer;
import athora.objects.AthoraEnemy;
import athora.objects.AthoraObject;
import athora.player.AthoraPlayer;

import java.util.*;

import static athora.assets.AthoraAssets.*;

public class AthoraLogic {

    public static Scanner input = new Scanner(System.in);
    public static int playerHealth = 10;
    public static ArrayList<AthoraObject> inventory = new ArrayList<>();

    public static AthoraPlayer player = new AthoraPlayer(playerHealth, inventory);

    public static AthoraMap map;

    public static void startGame(AthoraMap map) {

        AthoraLogic.map = map;

        if (map == null) {
            System.out.println("Error initiating map.");
            return;
        }

        System.out.println('\n' + map.getTitle() + '\n');

        System.out.println(look(map));

        main:
        while (player.getHealth() > 0) {

            System.out.print("> " + ANSI_GREEN);
            String command = input.nextLine().toLowerCase().trim();
            String verb = getVerb(command);

            System.out.print(ANSI_RESET);

            String args = command.replace(verb, "").toLowerCase().trim();

            switch (verb) {
                case "look" -> System.out.println(look(map));
                case "north", "east", "south", "west", "up", "down" -> move(map, verb);
                case "move", "go", "walk" -> {
                    String direction = getVerb(args);
                    if (direction == null) System.out.println("Where do you want to move?");
                    else move(map, direction);
                }
                case "pick", "pickup", "take" -> {
                    if (args.contains("out") || args.contains("from")) {
                        player.removeFromContents(args, player.getContainer(args));
                        break;
                    }
                    player.pickup(args);
                }
                case "exit", "stop" -> {
                    break main;
                }
                case "drop", "rid" -> player.drop(args);
                case "eat", "consume", "drink" -> player.eat(args);
                case "put", "place", "insert" -> {
                    if (!args.contains("in")) {
                        System.out.println("Specify what you want to put that in.");
                        break;
                    }
                    player.addToContents(args, player.getContainer(args));
                }
                case "remove" -> {
                    if (!args.contains("out") || !args.contains("from")) {
                        System.out.println("Specify what you want to take that out of.");
                        break;
                    }
                    player.removeFromContents(args, player.getContainer(args));
                }
                case "inv", "inventory", "items", "health", "hp" -> {
                    StringBuilder inventoryString = new StringBuilder();
                    for (AthoraObject item : inventory) {
                        if (!(item instanceof AthoraContainer c))
                            inventoryString.append("\n").append("* ").append(item.getName()).trimToSize();
                        else {
                            inventoryString.append("\n").append("* ").append(item.getName()).append(" (to take items out type remove)").trimToSize();
                            for (AthoraObject o : c.getContents()) {
                                inventoryString.append("\n   - ").append(o.getName());
                            }
                        }
                    }
                    if (inventoryString.isEmpty()) inventoryString.append("(none)");
                    System.out.println("Inventory: " + inventoryString + "\nHealth: " + player.getHealth());
                }
                case "kill", "attack", "knife", "stab", "hit", "murder" -> {
                    if (!args.contains("with") || !args.contains("using")) {
                        System.out.println("Specify what you want to attack with.");
                        break;
                    }
                    player.swing(args, player.getObj(args));
                }
                default -> System.out.println("I don't understand \"" + command + "\".");
            }
        }

        System.out.println(AthoraAssets.diedMessage);

    }

    public static String look(AthoraMap map) {
        List<AthoraObject> placeObjects = map.getCurrentScene().getObjs();
        if (placeObjects.isEmpty())
            return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + "There are no items here.";
        StringJoiner objects = new StringJoiner(", a ", "There is a ", " here.");
        placeObjects.forEach(o->objects.add(o.getName()));
        return map.getCurrentScene().getName() + "\n" + map.getCurrentScene().getSetting() + "\n" + objects;
    }

    public static void move(AthoraMap map, String direction) {
        AthoraDirection dir = map.getCurrentScene().getDestinations().get(AthoraDirection.getIndex(direction));
        if (map.findScene(dir) == null) {
            if(dir.getHealth() == null) System.out.println(dir.getMessage());
            else {
                System.out.println(dir.getMessage() + " " + dir.getHealth() + " HP");
                player.changeHealth(dir.getHealth());
            }
            return;
        }
        if (map.getCurrentScene().getObjs().stream().anyMatch(o -> o instanceof AthoraEnemy && ((AthoraEnemy) o).getBlocking().stream().anyMatch(b -> b.equals(dir.getId())) && ((AthoraEnemy) o).isAlive())) {
            System.out.println("There is an enemy blocking your path.");
            return;
        }
        map.setCurrentScene(map.findScene(dir));
        System.out.println(look(map));
    }

    public static String getVerb(String input) {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split(" ")));
        Optional<String> match = Arrays.stream(verbs).filter(args::contains).findFirst();
        return match.orElse("none");
    }

}