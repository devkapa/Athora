package com.nulfy.athora.scenes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record AthoraScene(long id, String name, String setting, List<Map<String, ?>> directions) {

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
            default -> System.out.println("There is no direction in that sentence.");
        }
        return 100;
    }

    public void moveTo(long scene) {
        currentScene = athoraScenes.get((int) scene);
    }

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();
    public static AthoraScene currentScene;

    @SuppressWarnings("unchecked")
    public static void InitiateScenes(String scenesPath) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONArray scenes = (JSONArray) parser.parse(new FileReader(scenesPath));

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

            AthoraScene selectedScene = new AthoraScene(
                    (long) s.get("id"),
                    (String) s.get("name"),
                    (String) s.get("setting"),
                    directions
            );

            athoraScenes.add(selectedScene);

        }

        currentScene = athoraScenes.get(0);

    }

}

