package com.nulfy.athora.objects;

public class AthoraObstacle extends AthoraObject {

    private final long damage;
    private long health;
    private final int[] positions;

    public AthoraObstacle(long id, String name, String type, boolean accessible, long damage, long health, int[] positions) {
        super(id, name, type, accessible);
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

    public int[] getPositions() {
        return positions;
    }
}
