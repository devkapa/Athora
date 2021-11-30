package com.nulfy.athora.objects;

public class AthoraWeapon extends AthoraObject {

    private final long damage;

    public AthoraWeapon(long id, String name, String type, long damage) {
        super(id, name, type);
        this.damage = damage;
    }

    public long getDamage() {
        return damage;
    }

}
