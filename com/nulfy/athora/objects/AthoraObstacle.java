package com.nulfy.athora.objects;

public class AthoraObstacle extends AthoraObject {

    private final int damage;
    private int health;

    public AthoraObstacle(long id, String name, String type, boolean accessible, int damage, int health) {
        super(id, name, type, accessible);
        this.damage = damage;
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
    }

}
