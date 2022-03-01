package athora.map;

import athora.objects.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class AthoraMap {

    public final String name;
    public final String title;
    public final ArrayList<AthoraPlace> places;
    public AthoraPlace currentPlace;

    public AthoraMap(String name, String title, ArrayList<AthoraPlace> places, AthoraPlace currentPlace){
        this.name = name;
        this.title = title;
        this.places = places;
        this.currentPlace = currentPlace;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<AthoraPlace> getPlaces() {
        return places;
    }

    public void setCurrentPlace(AthoraPlace currentPlace) {
        this.currentPlace = currentPlace;
    }

    public AthoraPlace getCurrentPlace() {
        return currentPlace;
    }

    public AthoraPlace findPlaceByCoords(AthoraDirection dir) {
        int[] currentPlaceCoords = currentPlace.getCoords();
        int[] newPlaceCoords = currentPlaceCoords.clone();
        newPlaceCoords[dir.index()] += dir.value();
        return places.stream().filter(place -> Arrays.equals(place.getCoords(), newPlaceCoords)).findFirst().orElse(null);
    }

    public static AthoraMap getMap() {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream defaultMap = AthoraPlace.class.getResourceAsStream("/AthoraDefaultMap.xml");

            Document mapFile = builder.parse(defaultMap);
            mapFile.getDocumentElement().normalize();

            String mapName = mapFile.getDocumentElement().getAttribute("name");
            String mapSplash = mapFile.getElementsByTagName("splash").item(0).getTextContent().trim();

            NodeList scenes = mapFile.getElementsByTagName("scene");

            ArrayList<AthoraPlace> places = new ArrayList<>();

            for (int i = 0; i < scenes.getLength(); i++){

                Node scene = scenes.item(i);

                if(scene.getNodeType() == Node.ELEMENT_NODE){
                    Element sceneElement = (Element) scene;

                    String sceneName = sceneElement.getAttribute("name");
                    String sceneSetting = sceneElement.getElementsByTagName("setting").item(0).getTextContent().trim();
                    NamedNodeMap sceneCoords = sceneElement.getElementsByTagName("coords").item(0).getAttributes();
                    int[] sceneXyz = {
                            Integer.parseInt(sceneCoords.getNamedItem("x").getTextContent()),
                            Integer.parseInt(sceneCoords.getNamedItem("y").getTextContent()),
                            Integer.parseInt(sceneCoords.getNamedItem("z").getTextContent())
                    };

                    ArrayList<AthoraInvItem> sceneItems = new ArrayList<>();

                    NodeList items = sceneElement.getElementsByTagName("item");

                    for (int j = 0; j < items.getLength(); j++){

                        Node item = items.item(j);

                        if(scene.getNodeType() == Node.ELEMENT_NODE) {
                            Element itemElement = (Element) item;

                            NamedNodeMap itemStats = itemElement.getElementsByTagName("stats").item(0).getAttributes();
                            NamedNodeMap itemTake = itemElement.getElementsByTagName("take").item(0).getAttributes();

                            String itemName = itemElement.getElementsByTagName("name").item(0).getTextContent();
                            boolean itemTakeBool = Boolean.parseBoolean(itemTake.getNamedItem("bool").getTextContent());
                            long itemMass = Long.parseLong(itemStats.getNamedItem("mass").getTextContent());
                            long itemDamage = Long.parseLong(itemStats.getNamedItem("damage").getTextContent());

                            switch(itemElement.getAttribute("type")){
                                case "food" -> {
                                    long itemSaturation = Long.parseLong(itemStats.getNamedItem("saturation").getTextContent());
                                    sceneItems.add(new AthoraFood(itemName, "food", itemTakeBool, itemMass, itemDamage, itemSaturation));
                                }
                                case "container" -> {
                                    long itemMaxMass = Long.parseLong(itemStats.getNamedItem("max-mass").getTextContent());
                                    sceneItems.add(new AthoraContainer(itemName, "container", itemTakeBool, itemMass, itemDamage, itemMaxMass, new ArrayList<>()));
                                }
                                case "object" -> sceneItems.add(new AthoraObject(itemName, "object", itemTakeBool, itemMass, itemDamage));
                                //case "obstacle" -> {
                                //    long itemHealth = Long.parseLong(itemStats.getNamedItem("health").getTextContent());
                                //    sceneItems.add(new AthoraObstacle(itemName, "obstacle", itemTake, itemMass, itemDamage, itemHealth));
                                //}
                                case "weapon" -> {
                                    String itemEvent = itemElement.getElementsByTagName("take").item(0).getTextContent();
                                    sceneItems.add(new AthoraWeapon(itemName, "weapon", itemTakeBool, itemMass, itemDamage, itemEvent));
                                }
                            }

                        }

                    }

                    places.add(new AthoraPlace(sceneName, sceneSetting, sceneXyz, sceneItems));

                }

            }

            return new AthoraMap(mapName, mapSplash, places, places.get(0));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;

    }


}
