package com.nulfy.athora.objects;

public class AthoraWeapon extends AthoraObject {

    private final int damage;

    public AthoraWeapon(long id, String name, String type, boolean accessible, int damage) {
        super(id, name, type, accessible);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void attack(AthoraObstacle enemy, AthoraWeapon weapon) {
        enemy.changeHealth(weapon.getDamage());
    }

}
