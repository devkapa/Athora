package athora.objects;

import java.util.List;

public class AthoraEnemy extends AthoraObject {

    private int health;
    private final List<Integer> blocking;

    // Enemy constructor, to be used when initiating a new enemy object.
    public AthoraEnemy(String name, boolean accessible, int mass, int damage, int health, List<Integer> blocking) {
        super(name, accessible, mass, damage);
        this.health = health;
        this.blocking = blocking;
    }

    // Get the health of the enemy
    public int getHealth() {
        return health;
    }

    // Change the health of the enemy by the provided amount in parameters
    public void changeHealth(int amount) {
        health += amount;
    }

    // Return a true or false value if the enemy is alive
    public boolean isAlive() {
        return health > 0;
    }

    // Get an integer list of scene IDs that the enemy is blocking
    public List<Integer> getBlocking() {
        return blocking;
    }

}
