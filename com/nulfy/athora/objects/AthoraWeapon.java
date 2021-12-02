package com.nulfy.athora.objects;

import com.nulfy.athora.player.AthoraPlayer;

import java.util.Arrays;
import java.util.Iterator;

import static com.nulfy.athora.scenes.AthoraScene.currentScene;

public class AthoraWeapon extends AthoraObject {

    private final long damage;

    public AthoraWeapon(long id, String name, String type, boolean accessible, long damage) {
        super(id, name, type, accessible);
        this.damage = damage;
    }

    public long getDamage() {
        return damage;
    }

    public void attack(AthoraObstacle enemy, AthoraWeapon weapon) {
        enemy.changeHealth(weapon.getDamage());
    }

}
