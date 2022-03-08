package athora.objects;

public class AthoraObject {

    private String name;
    private final boolean accessible;
    private final int mass;
    private final int damage;

    public AthoraObject(String name, boolean accessible, int mass, int damage) {
        this.name = name;
        this.accessible = accessible;
        this.mass = mass;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMass() {
        return mass;
    }

    public int getDamage() {
        return damage;
    }

    public void attack(AthoraEnemy enemy, AthoraObject weapon) {
        enemy.changeHealth(-weapon.getDamage());
    }

    public boolean isAccessible() {
        return accessible;
    }

}
