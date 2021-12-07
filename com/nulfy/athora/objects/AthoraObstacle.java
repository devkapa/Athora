package com.nulfy.athora.objects;

import org.json.simple.JSONArray;

public class AthoraObstacle extends AthoraObject {

    private final long damage;
    private long health;
    private final JSONArray positions;

    public AthoraObstacle(String name, String type, boolean accessible, long damage, long health, JSONArray positions) {
        super(name, type, accessible);
        this.damage = damage;
        this.health = health;
        this.positions = positions;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public long getDamage() {
        return damage;
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
