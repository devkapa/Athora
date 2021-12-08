package com.nulfy.athora.objects;

import com.nulfy.athora.player.AthoraPlayer;

import static com.nulfy.athora.scenes.AthoraScene.currentScene;

public class AthoraWeapon extends AthoraObject {

    private final String event;
    private final long damage;

    public AthoraWeapon(String name, String type, boolean accessible, long mass, long damage, String event) {
        super(name, type, accessible, mass, damage);
        this.event = event;
        this.damage = damage;
    }

    public void executeEvent(AthoraPlayer player) {
        player.changeHealth((int) damage);
        System.out.println(event + " " + damage + " HP");
    }

}
