package athora.objects;

import static athora.scenes.AthoraScene.currentScene;

public class AthoraObject {

    private final String name;
    private final String type;
    private boolean accessible;
    private final long mass;
    private final long damage;

    public AthoraObject(String name, String type, boolean accessible, long mass, long damage) {
        this.name = name;
        this.type = type;
        this.accessible = accessible;
        this.mass = mass;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public long getMass() {
        return mass;
    }

    public String getType() {
        return type;
    }

    public long getDamage() {
        return damage;
    }

    public void attack(AthoraObstacle enemy, AthoraObject weapon) {
        enemy.changeHealth(weapon.getDamage());
        if(!enemy.isAlive()) currentScene.objects().remove(enemy);
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void accessible(boolean bool) { accessible = bool; }

}
