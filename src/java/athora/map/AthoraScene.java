package athora.map;

import athora.objects.AthoraObject;

import java.util.ArrayList;
import java.util.List;

public record AthoraScene(int id, String name, String setting, List<AthoraDirection> destinations, ArrayList<AthoraObject> items) {

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSetting() {
        return setting;
    }

    public List<AthoraDirection> getDestinations() {
        return destinations;
    }

    public ArrayList<AthoraObject> getObjs() {
        return items;
    }

}
