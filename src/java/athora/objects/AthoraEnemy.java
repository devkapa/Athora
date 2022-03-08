package athora.objects;

import java.util.List;

public class AthoraEnemy extends AthoraInvItem {

    private int health;
    private final List<Integer> blocking;

    public AthoraEnemy(String name, boolean accessible, int mass, int damage, int health, List<Integer> blocking) {
        super(name, accessible, mass, damage);
        this.health = health;
        this.blocking = blocking;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
    }

    public List<Integer> getBlocking() {
        return blocking;
    }

}
