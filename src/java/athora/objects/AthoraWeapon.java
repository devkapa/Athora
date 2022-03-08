package athora.objects;

import athora.player.AthoraPlayer;

public class AthoraWeapon extends AthoraObject {

    private final String event;
    private final int damage;

    public AthoraWeapon(String name, boolean accessible, int mass, int damage, String event) {
        super(name, accessible, mass, damage);
        this.event = event;
        this.damage = damage;
    }

    public void executeEvent(AthoraPlayer player) {
        player.changeHealth(-damage);
        System.out.println(event + " -" + damage + " HP");
    }

}
