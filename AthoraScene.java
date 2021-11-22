import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AthoraScene {

    long id;
    String name;
    ArrayList<Long> directions;
    String setting;

    public AthoraScene(long id, String name, ArrayList<Long> directions, String setting) {
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

    public ArrayList<Long> getDirections() {
        return directions;
    }

    public String getSetting(){
        return setting;
    }

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();
    public static AthoraScene currentScene;

    public static void InitiateScenes() throws IOException, ParseException {

        String jsonPath = "AthoraScenes.json";

        JSONParser parser = new JSONParser();

        JSONArray scenes = (JSONArray) parser.parse(new FileReader(jsonPath));

        for (Object scene : scenes) {

            JSONObject s = (JSONObject) scene;

            ArrayList<Long> directions = new ArrayList<>();
            directions.add((long) s.get("north"));
            directions.add((long) s.get("east"));
            directions.add((long) s.get("south"));
            directions.add((long) s.get("west"));

            AthoraScene selectedScene = new AthoraScene(
                    (long) s.get("id"),
                    (String) s.get("name"),
                    directions,
                    (String) s.get("setting")
            );

            athoraScenes.add(selectedScene);

        }

        currentScene = athoraScenes.get(0);

    }

}

