package com.nulfy.athora.objects;

import com.nulfy.athora.player.AthoraPlayer;

public class AthoraInventoryItem extends AthoraObject {

    public AthoraInventoryItem(long id, String name, String type, boolean accessible) {
        super(id, name, type, accessible);
    }

    public void pickup(AthoraInventoryItem item, AthoraPlayer player) {
        player.getInventory().add(item);
        // TODO: Remove item from scene, add item handling to AthoraScene
        System.out.println("You picked up " + item.getName());
    }

    public void drop(AthoraInventoryItem item, AthoraPlayer player) {
        player.getInventory().remove(item);
        // TODO: Add item to scene, add item handling to AthoraScene
        System.out.println("Dropped " + item.getName());
    }

}
