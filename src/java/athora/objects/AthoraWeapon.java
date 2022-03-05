package athora.objects;

import athora.player.AthoraPlayer;

import static athora.assets.AthoraAssets.ANSI_RESET;

public class AthoraWeapon extends AthoraInvItem {

    private final String event;
    private final int damage;

    public AthoraWeapon(String name, boolean accessible, int mass, int damage, String event) {
        super(name, accessible, mass, damage);
        this.event = event;
        this.damage = damage;
    }

    public void executeEvent(AthoraPlayer player) {
        player.changeHealth(-damage);
        System.out.println(ANSI_RESET + event + " -" + damage + " HP");
    }

}
