package athora.objects;

import org.json.simple.JSONArray;

public class AthoraObstacle extends AthoraInvItem {

    private long health;
    private final JSONArray positions;

    public AthoraObstacle(String name, String type, boolean accessible, long mass, long damage, long health, JSONArray positions) {
        super(name, type, accessible, mass, damage);
        this.health = health;
        this.positions = positions;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public long getHealth() {
        return health;
    }

    public void changeHealth(long amount) {
        health += amount;
    }

    public JSONArray getPositions() {
        return positions;
    }

}
