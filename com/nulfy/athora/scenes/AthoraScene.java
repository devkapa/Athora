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

public class AthoraScene {

    private final long id;
    private final String name;
    private final List<Map<?, ?>> directions;
    private final String setting;

    public AthoraScene(long id, String name, String setting, List<Map<?, ?>> directions) {
        this.id = id;
        this.name = name;
        this.directions = directions;
        this.setting = setting;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Map<?, ?>> getDirections() {
        return directions;
    }

    public String getDirectionMessage(int index) {
        return (String) directions.get(index).get("message");
    }

    public String getSetting(){
        return setting;
    }

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();
    public static AthoraScene currentScene;

    public static void InitiateScenes(String scenesPath) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        JSONArray scenes = (JSONArray) parser.parse(new FileReader(scenesPath));

        for (Object scene : scenes) {

            JSONObject s = (JSONObject) scene;

            JSONObject directionMap = new JSONObject((Map<?, ?>) s.get("directions"));

            List<Map<?, ?>> directions = Arrays.asList(
                    (Map<?, ?>) directionMap.get("north"),
                    (Map<?, ?>) directionMap.get("east"),
                    (Map<?, ?>) directionMap.get("south"),
                    (Map<?, ?>) directionMap.get("west")
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

