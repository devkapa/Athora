package com.nulfy.athora.objects;

public class AthoraObstacle extends AthoraObject {

    private final long damage;
    private long health;

    public AthoraObstacle(long id, String name, String type, boolean accessible, long damage, long health) {
        super(id, name, type, accessible);
        this.damage = damage;
        this.health = health;
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

}
