package athora.map;

import athora.objects.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class AthoraMap {

    public final String name;
    public final String title;
    public final ArrayList<AthoraScene> scenes;
    public AthoraScene currentScene;

    // Map constructor, used to initiate a new AthoraMap that can be passed to the startGame method
    public AthoraMap(String name, String title, ArrayList<AthoraScene> scenes, AthoraScene currentScene){
        this.name = name;
        this.title = title;
        this.scenes = scenes;
        this.currentScene = currentScene;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setCurrentScene(AthoraScene currentScene) {
        this.currentScene = currentScene;
    }

    public AthoraScene getCurrentScene() {
        return currentScene;
    }

    // Returns an AthoraScene object if the AthoraDirection object directly corresponds to a scene ID
    public AthoraScene findScene(AthoraDirection dir){
        if(dir.getId() != null){
            return scenes.stream().filter(scene->dir.getId() == scene.getId()).findFirst().orElse(null);
        }
        return null;
    }

    // Converts a local Athora Map file (.athora) to an AthoraMap object
    public static AthoraMap getMap(File defaultMap) {

        try {

            // Create a DocumentBuilder to read the XML syntax
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setNamespaceAware(true);

            // Parse the XML document using the builder
            Document mapFile = builder.parse(defaultMap);
            mapFile.getDocumentElement().normalize();

            // Get the name and splash from map contents
            String mapName = mapFile.getDocumentElement().getAttribute("name");
            String mapSplash = trimmed(mapFile.getElementsByTagName("splash").item(0).getTextContent());

            NodeList mapScenes = mapFile.getElementsByTagName("scene");

            ArrayList<AthoraScene> scenes = new ArrayList<>();

            // For every <scene> Node in the map, create a new Scene object
            for (int i = 0; i < mapScenes.getLength(); i++){

                Node mapScene = mapScenes.item(i);

                if(mapScene.getNodeType() == Node.ELEMENT_NODE){
                    Element sceneElement = (Element) mapScene;

                    // Get the name and setting of the scene from map contents
                    String sceneName = sceneElement.getAttribute("name");
                    String sceneSetting = trimmed(sceneElement.getElementsByTagName("setting").item(0).getTextContent());
                    int sceneId = Integer.parseInt(sceneElement.getAttribute("id"));

                    NodeList directions = sceneElement.getElementsByTagName("directions").item(0).getChildNodes();
                    ArrayList<AthoraDirection> sceneDirections = new ArrayList<>();

                    // For each direction in NodeList of directions, convert its values to an AthoraDirection
                    // object, and then add it to the ArrayList of scene directions
                    for (int j = 0; j < directions.getLength(); j++){

                        switch (directions.item(j).getNodeName()){
                            case "north" -> sceneDirections.add(0, setDirection(directions.item(j)));
                            case "east" -> sceneDirections.add(1, setDirection(directions.item(j)));
                            case "south" -> sceneDirections.add(2, setDirection(directions.item(j)));
                            case "west" -> sceneDirections.add(3, setDirection(directions.item(j)));
                            case "up" -> sceneDirections.add(4, setDirection(directions.item(j)));
                            case "down" -> sceneDirections.add(5, setDirection(directions.item(j)));
                        }

                    }

                    ArrayList<AthoraObject> sceneItems = new ArrayList<>();
                    NodeList items = sceneElement.getElementsByTagName("item");

                    // For each item in NodeList of objects, get the item's name, type and stats
                    // and then convert it to its corresponding AthoraObject type
                    for (int j = 0; j < items.getLength(); j++){

                        Node item = items.item(j);

                        if(mapScene.getNodeType() == Node.ELEMENT_NODE) {
                            Element itemElement = (Element) item;

                            // Get the name of the item
                            String itemName = itemElement.getElementsByTagName("name").item(0).getTextContent();

                            // Get the stats of the item
                            NamedNodeMap itemStats = itemElement.getElementsByTagName("stats").item(0).getAttributes();
                            NamedNodeMap itemTake = itemElement.getElementsByTagName("take").item(0).getAttributes();

                            boolean itemTakeBool = Boolean.parseBoolean(itemTake.getNamedItem("bool").getTextContent());
                            int itemMass = Integer.parseInt(itemStats.getNamedItem("mass").getTextContent());
                            int itemDamage = Integer.parseInt(itemStats.getNamedItem("damage").getTextContent());

                            // Create a new AthoraObject object for the item, depending on its type
                            switch(itemElement.getAttribute("type")){
                                case "food" -> {
                                    // If it is a food item, get its saturation points
                                    int itemSaturation = Integer.parseInt(itemStats.getNamedItem("saturation").getTextContent());
                                    sceneItems.add(new AthoraFood(itemName, itemTakeBool, itemMass, itemDamage, itemSaturation));
                                }
                                case "container" -> {
                                    // If it is a container item, get the maximum mass it can hold
                                    int itemMaxMass = Integer.parseInt(itemStats.getNamedItem("max-mass").getTextContent());
                                    sceneItems.add(new AthoraContainer(itemName, itemTakeBool, itemMass, itemDamage, itemMaxMass, new ArrayList<>()));
                                }
                                case "object" -> sceneItems.add(new AthoraObject(itemName, itemTakeBool, itemMass, itemDamage));
                                case "enemy" -> {
                                    // If it is an enemy item, get an integer list of the scenes it is blocking
                                    List<Integer> itemBlocking = new ArrayList<>();
                                    int itemHealth = Integer.parseInt(itemStats.getNamedItem("health").getTextContent());
                                    if(!itemElement.getElementsByTagName("blocking").item(0).getTextContent().equals("")){
                                        List<String> blockingInts = Arrays.asList(itemElement.getElementsByTagName("blocking").item(0).getTextContent().split(","));
                                        blockingInts.forEach(d->itemBlocking.add(Integer.parseInt(d)));
                                    }
                                    sceneItems.add(new AthoraEnemy(itemName, itemTakeBool, itemMass, itemDamage, itemHealth, itemBlocking));
                                }
                                case "weapon" -> {
                                    // If it is a weapon item, get its event
                                    String itemEvent = trimmed(itemElement.getElementsByTagName("take").item(0).getTextContent());
                                    sceneItems.add(new AthoraWeapon(itemName, itemTakeBool, itemMass, itemDamage, itemEvent));
                                }
                            }

                        }

                    }

                    // Create a new AthoraScene and add it to the list of scenes in the map
                    scenes.add(new AthoraScene(sceneId, sceneName, sceneSetting, sceneDirections, sceneItems));

                }

            }

            // Return the AthoraMap with its name, splash, scenes and current scene
            return new AthoraMap(mapName, mapSplash, scenes, scenes.get(0));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    // Gives the player a list of all maps in the game folder, and returns it
    // as a File that can be passed to the getMap method.
    public static File chooseMap() {

        while(true) {

            // Get user input
            Scanner input = new Scanner(System.in);

            // Get the maps folder and add all maps to a list
            File mapsFolder = new File("./maps");
            File[] mapsFolderFiles = mapsFolder.listFiles();

            ArrayList<File> maps = new ArrayList<>();

            if(mapsFolderFiles != null){
                for (File map : mapsFolderFiles) {
                    if (map.getName().toLowerCase().endsWith(".athora")) {
                        maps.add(map);
                    }
                }
            }

            // Display all available maps to the user
            System.out.println("Please choose which map you would like to play:");

            for(File map : maps){
                try {
                    // Read the name of the map and print it with the file name
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document mapFile = builder.parse(map);
                    String mapName = mapFile.getDocumentElement().getAttribute("name");
                    System.out.println(maps.indexOf(map) + ": " + mapName + " (" + map.getName() + ")");
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }

            // Await user input
            System.out.print("> ");

            // If user input is not an integer
            if(!input.hasNextInt()){
                System.out.println("Enter a valid number.");
                continue;
            }

            int chosen = input.nextInt();

            // If the integer does not represent a map in the list
            if(chosen >= maps.size()){
                System.out.println("That is not an option in the maps list.");
                continue;
            }

            // Return the map file by its index
            return maps.get(chosen);
        }

    }

    // Trim excessive whitespaces in a multi-lined String
    public static String trimmed(String toTrim){
        // Split the String by each new line
        String[] split = toTrim.split("\n");
        StringJoiner joiner = new StringJoiner("\n");
        // Trim and rejoin each line
        for(String s : split){
            joiner.add(s.trim());
        }
        return joiner.toString().trim();
    }

    // Return an AthoraDirection object depending on what the direction node represents
    public static AthoraDirection setDirection(Node node){
        // If the direction node represents a scene ID
        if(node.getTextContent().equals("")){
            int id = Integer.parseInt(node.getAttributes().getNamedItem("destination").getTextContent());
            return new AthoraDirection(id);
        }
        // If the direction node represents both a String message and a health change
        if(node.getAttributes().getNamedItem("health") != null){
            String message = trimmed(node.getTextContent());
            int health = Integer.parseInt(node.getAttributes().getNamedItem("health").getTextContent());
            return new AthoraDirection(message, health);
        } else {
            // If the direction node represents only a String message
            String message = trimmed(node.getTextContent());
            return new AthoraDirection(message);
        }
    }

}
