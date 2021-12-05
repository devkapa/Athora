package com.nulfy.athora.scenes;

import com.nulfy.athora.objects.AthoraInventoryItem;
import com.nulfy.athora.objects.AthoraObject;
import com.nulfy.athora.objects.AthoraObstacle;
import com.nulfy.athora.objects.AthoraWeapon;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public record AthoraScene(long id, String name, String setting, List<Map<String, ?>> directions, ArrayList<AthoraObject> objects) {

    public String getName() {
        return name;
    }

    public long getDirectionValue(int index) {
        return (long) directions.get(index).get("value");
    }

    public long getDirectionHealthChange(int index) {
        if (directions.get(index).get("hp") != null) return (long) directions.get(index).get("hp");
        else return 100;
    }

    public ArrayList<AthoraObject> objects() {
        return objects;
    }

    public String getDirectionMessage(int index) {
        return (String) directions.get(index).get("message");
    }

    public String getSetting() {
        return setting;
    }

    public int indexFromDirection(String direction) {
        switch (direction) {
            case "north" -> {
                return 0;
            }
            case "east" -> {
                return 1;
            }
            case "south" -> {
                return 2;
            }
            case "west" -> {
                return 3;
            }
            case "up" -> {
                return 4;
            }
            case "down" -> {
                return 5;
            }
            default -> {
                System.out.println("Where do you want to go?");
                return 100;
            }
        }
    }

    public void moveTo(long scene) {
        currentScene = athoraScenes.get((int) scene);
    }

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();
    public static AthoraScene currentScene;

    @SuppressWarnings("unchecked")
    public static void initiateScenes(String scenesPath, String objectsPath) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONArray scenes = (JSONArray) parser.parse(new FileReader(scenesPath));
        JSONObject objects = (JSONObject) parser.parse(new FileReader(objectsPath));

        for (Object scene : scenes) {

            JSONObject s = (JSONObject) scene;

            JSONObject directionMap = new JSONObject((Map<String, ?>) s.get("directions"));

            List<Map<String, ?>> directions = Arrays.asList(
                    (Map<String, ?>) directionMap.get("north"),
                    (Map<String, ?>) directionMap.get("east"),
                    (Map<String, ?>) directionMap.get("south"),
                    (Map<String, ?>) directionMap.get("west"),
                    (Map<String, ?>) directionMap.get("up"),
                    (Map<String, ?>) directionMap.get("down")
            );

            JSONArray objectsArray = (JSONArray) objects.get(s.get("id").toString());
            ArrayList<AthoraObject> objectArrayList = new ArrayList<>();

            if(objectsArray != null){
                for (Object object : objectsArray) {
                    JSONObject o = (JSONObject) object;
                    switch ((String) o.get("type")) {
                        case "weapon" -> objectArrayList.add(
                                new AthoraWeapon((long) s.get("id"), (String) o.get("name"), "weapon", (boolean) o.get("accessible"), (long) o.get("damage"), (String) o.get("event"))
                        );
                        case "item" -> objectArrayList.add(
                                new AthoraInventoryItem((long) s.get("id"), (String) o.get("name"), "item", (boolean) o.get("accessible"))
                        );
                        case "obstacle" -> objectArrayList.add(
                                new AthoraObstacle((long) s.get("id"), (String) o.get("name"), "obstacle", (boolean) o.get("accessible"), (long) o.get("damage"), (long) o.get("health"))
                        );
                    }
                }
            }

            AthoraScene selectedScene = new AthoraScene(
                    (long) s.get("id"),
                    (String) s.get("name"),
                    (String) s.get("setting"),
                    directions,
                    objectArrayList
            );

            athoraScenes.add(selectedScene);

        }

        currentScene = athoraScenes.get(0);

    }

}

