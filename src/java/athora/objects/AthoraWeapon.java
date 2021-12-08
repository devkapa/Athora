package athora.objects;

import athora.player.AthoraPlayer;

import static athora.assets.AthoraAssets.ANSI_RESET;

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
        System.out.println(ANSI_RESET + event + " " + damage + " HP");
    }

}
