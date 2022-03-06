package athora.map;

import athora.objects.AthoraInvItem;

import java.util.ArrayList;
import java.util.List;

public record AthoraScene(int id, String name, String setting, List<AthoraDirection> destinations, ArrayList<AthoraInvItem> items) {

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

    public ArrayList<AthoraInvItem> getObjs() {
        return items;
    }

}
