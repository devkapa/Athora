import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AthoraLogic {

    public static ArrayList<AthoraScene> athoraScenes = new ArrayList<>();

    public static void run() throws IOException, ParseException {

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
    }

    public static void main(String[] args) throws IOException, ParseException {
        run();

        System.out.println(athoraScenes);
    }

}