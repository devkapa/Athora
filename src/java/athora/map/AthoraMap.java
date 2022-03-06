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

    public AthoraMap(String name, String title, ArrayList<AthoraScene> scenes, AthoraScene currentScene){
        this.name = name;
        this.title = title;
        this.scenes = scenes;
        this.currentScene = currentScene;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setCurrentScene(AthoraScene currentScene) {
        this.currentScene = currentScene;
    }

    public AthoraScene getCurrentScene() {
        return currentScene;
    }

    public AthoraScene findScene(AthoraDirection dir){
        if(dir.getId() != null){
            return scenes.stream().filter(scene->dir.getId() == scene.getId()).findFirst().orElse(null);
        }
        return null;
    }

    public static AthoraMap getMap(File defaultMap) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setNamespaceAware(true);

            Document mapFile = builder.parse(defaultMap);
            mapFile.getDocumentElement().normalize();

            String mapName = mapFile.getDocumentElement().getAttribute("name");
            String mapSplash = trimmed(mapFile.getElementsByTagName("splash").item(0).getTextContent());

            NodeList mapScenes = mapFile.getElementsByTagName("scene");

            ArrayList<AthoraScene> scenes = new ArrayList<>();

            for (int i = 0; i < mapScenes.getLength(); i++){

                Node mapScene = mapScenes.item(i);

                if(mapScene.getNodeType() == Node.ELEMENT_NODE){
                    Element sceneElement = (Element) mapScene;

                    String sceneName = sceneElement.getAttribute("name");
                    String sceneSetting = trimmed(sceneElement.getElementsByTagName("setting").item(0).getTextContent());

                    NodeList directions = sceneElement.getElementsByTagName("directions").item(0).getChildNodes();
                    ArrayList<AthoraDirection> sceneDirections = new ArrayList<>();

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

                    ArrayList<AthoraInvItem> sceneItems = new ArrayList<>();

                    NodeList items = sceneElement.getElementsByTagName("item");
                    int sceneId = Integer.parseInt(sceneElement.getAttribute("id"));

                    for (int j = 0; j < items.getLength(); j++){

                        Node item = items.item(j);

                        if(mapScene.getNodeType() == Node.ELEMENT_NODE) {
                            Element itemElement = (Element) item;

                            NamedNodeMap itemStats = itemElement.getElementsByTagName("stats").item(0).getAttributes();
                            NamedNodeMap itemTake = itemElement.getElementsByTagName("take").item(0).getAttributes();

                            String itemName = itemElement.getElementsByTagName("name").item(0).getTextContent();
                            boolean itemTakeBool = Boolean.parseBoolean(itemTake.getNamedItem("bool").getTextContent());
                            int itemMass = Integer.parseInt(itemStats.getNamedItem("mass").getTextContent());
                            int itemDamage = Integer.parseInt(itemStats.getNamedItem("damage").getTextContent());

                            switch(itemElement.getAttribute("type")){
                                case "food" -> {
                                    int itemSaturation = Integer.parseInt(itemStats.getNamedItem("saturation").getTextContent());
                                    sceneItems.add(new AthoraFood(itemName, itemTakeBool, itemMass, itemDamage, itemSaturation));
                                }
                                case "container" -> {
                                    int itemMaxMass = Integer.parseInt(itemStats.getNamedItem("max-mass").getTextContent());
                                    sceneItems.add(new AthoraContainer(itemName, itemTakeBool, itemMass, itemDamage, itemMaxMass, new ArrayList<>()));
                                }
                                case "object" -> sceneItems.add(new AthoraObject(itemName, itemTakeBool, itemMass, itemDamage));
                                case "enemy" -> {
                                    List<Integer> itemBlocking = new ArrayList<>();
                                    int itemHealth = Integer.parseInt(itemStats.getNamedItem("health").getTextContent());
                                    if(!itemElement.getElementsByTagName("blocking").item(0).getTextContent().equals("")){
                                        List<String> blockingInts = Arrays.asList(itemElement.getElementsByTagName("blocking").item(0).getTextContent().split(","));
                                        blockingInts.forEach(d->itemBlocking.add(Integer.parseInt(d)));
                                    }
                                    sceneItems.add(new AthoraEnemy(itemName, itemTakeBool, itemMass, itemDamage, itemHealth, itemBlocking));
                                }
                                case "weapon" -> {
                                    String itemEvent = trimmed(itemElement.getElementsByTagName("take").item(0).getTextContent());
                                    sceneItems.add(new AthoraWeapon(itemName, itemTakeBool, itemMass, itemDamage, itemEvent));
                                }
                            }

                        }

                    }

                    scenes.add(new AthoraScene(sceneId, sceneName, sceneSetting, sceneDirections, sceneItems));

                }

            }

            return new AthoraMap(mapName, mapSplash, scenes, scenes.get(0));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static File chooseMap() {

        while(true) {

            Scanner input = new Scanner(System.in);

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

            System.out.println("\nPlease choose which map you would like to play:");

            for(File map : maps){
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document mapFile = builder.parse(map);
                    String mapName = mapFile.getDocumentElement().getAttribute("name");
                    System.out.println(maps.indexOf(map) + ": " + mapName + " (" + map.getName() + ")");
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.print("> ");

            if(!input.hasNextInt()){
                System.out.println("Enter a valid number.");
                continue;
            }

            int chosen = input.nextInt();

            if(chosen >= maps.size()){
                System.out.println("That is not an option in the maps list.");
                continue;
            }

            return maps.get(chosen);
        }

    }

    public static String trimmed(String toTrim){
        String[] split = toTrim.split("\n");
        StringJoiner joiner = new StringJoiner("\n");
        for(String s : split){
            joiner.add(s.trim());
        }
        return joiner.toString().trim();
    }

    public static AthoraDirection setDirection(Node node){
        if(node.getTextContent().equals("")){
            int id = Integer.parseInt(node.getAttributes().getNamedItem("destination").getTextContent());
            return new AthoraDirection(id);
        }
        if(node.getAttributes().getNamedItem("health") != null){
            String message = trimmed(node.getTextContent());
            int health = Integer.parseInt(node.getAttributes().getNamedItem("health").getTextContent());
            return new AthoraDirection(message, health);
        } else {
            String message = trimmed(node.getTextContent());
            return new AthoraDirection(message);
        }
    }

}
