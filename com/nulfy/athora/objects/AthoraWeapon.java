package com.nulfy.athora.objects;

import com.nulfy.athora.player.AthoraPlayer;

import static com.nulfy.athora.scenes.AthoraScene.currentScene;

public class AthoraWeapon extends AthoraObject {

    private final long damage;
    private final String event;

    public AthoraWeapon(long id, String name, String type, boolean accessible, long damage, String event) {
        super(id, name, type, accessible);
        this.damage = damage;
        this.event = event;
    }

    public long getDamage() {
        return damage;
    }

    public void executeEvent(AthoraPlayer player) {
        player.changeHealth((int) damage);
        System.out.println(event + " " + damage + " HP");
    }

    public void attack(AthoraObstacle enemy, AthoraWeapon weapon) {
        enemy.changeHealth(weapon.getDamage());
        currentScene.objects().remove(enemy);
    }

}
