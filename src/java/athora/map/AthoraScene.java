package athora.map;

import athora.objects.AthoraInvItem;

import java.util.ArrayList;

public record AthoraScene(int id, String name, String setting, int[] coords, ArrayList<AthoraInvItem> items) {

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSetting() {
        return setting;
    }

    public int[] getCoords() {
        return coords;
    }

    public ArrayList<AthoraInvItem> getObjs() {
        return items;
    }

}
