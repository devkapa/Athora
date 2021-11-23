import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AthoraScene {

    private final long id;
    private final String name;
    private final ArrayList<Long> directions;
    private final String setting;

    private final String northMessage;
    private final String eastMessage;
    private final String southMessage;
    private final String westMessage;


    public AthoraScene(long id, String name, ArrayList<Long> directions, String setting, String northMessage, String eastMessage, String southMessage, String westMessage) {
        this.id = id;
        this.name = name;
        this.directions = directions;
        this.setting = setting;
        this.northMessage = northMessage;
        this.eastMessage = eastMessage;
        this.westMessage = westMessage;
        this.southMessage = southMessage;
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

    public String getDirectionMessage(int index) {
        if(index == 0){
            return northMessage;
        } else if(index == 1) {
            return eastMessage;
        }  else if(index == 2) {
            return southMessage;
        }  else if(index == 3) {
            return westMessage;
        }
        return null;
    }

    public String getSetting(){
        return setting;
    }

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();
    public static AthoraScene currentScene;

    public static AthoraScene getScene() { return currentScene; }

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
                    (String) s.get("setting"),
                    (String) s.get("northMessage"),
                    (String) s.get("eastMessage"),
                    (String) s.get("southMessage"),
                    (String) s.get("westMessage")
            );

            athoraScenes.add(selectedScene);

        }

        currentScene = athoraScenes.get(0);

    }

}

