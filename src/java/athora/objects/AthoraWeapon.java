package athora.objects;

import athora.player.AthoraPlayer;

public class AthoraWeapon extends AthoraObject {

    private final String event;
    private final int damage;

    // Weapon constructor, to be called when a new instance of a weapon is to be created
    public AthoraWeapon(String name, boolean accessible, int mass, int damage, String event) {
        super(name, accessible, mass, damage);
        this.event = event;
        this.damage = damage;
    }

    // Damage the player for trying to pick up the weapon
    public void executeEvent(AthoraPlayer player) {
        player.changeHealth(-damage);
        System.out.println(event + " -" + damage + " HP");
    }

}
