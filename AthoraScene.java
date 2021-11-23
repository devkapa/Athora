import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AthoraScene {

    private final long id;
    private final String name;
    private final List<Long> directions;
    private final String setting;

    private final List<String> directionMessages;

    public AthoraScene(long id, String name, String setting, List<Long> directions, List<String> directionMessages) {
        this.id = id;
        this.name = name;
        this.directions = directions;
        this.setting = setting;
        this.directionMessages = directionMessages;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Long> getDirections() {
        return directions;
    }

    public String getDirectionMessage(int index) {
        return directionMessages.get(index);
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

            List<Long> directions = Arrays.asList(
                    (long) s.get("north"),
                    (long) s.get("east"),
                    (long) s.get("south"),
                    (long) s.get("west")
            );

            List<String> directionMessages = Arrays.asList(
                    (String) s.get("northMessage"),
                    (String) s.get("eastMessage"),
                    (String) s.get("southMessage"),
                    (String) s.get("westMessage")
            );


            AthoraScene selectedScene = new AthoraScene(
                    (long) s.get("id"),
                    (String) s.get("name"),
                    (String) s.get("setting"),
                    directions,
                    directionMessages
            );

            athoraScenes.add(selectedScene);

        }

        currentScene = athoraScenes.get(0);

    }

}

