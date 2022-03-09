package athora.objects;

public class AthoraObject {

    private String name;
    private final boolean accessible;
    private final int mass;
    private final int damage;

    // Object constructor, to be used when initiating a new inventory object
    // This is also the base parent class for all other types of objects
    public AthoraObject(String name, boolean accessible, int mass, int damage) {
        this.name = name;
        this.accessible = accessible;
        this.mass = mass;
        this.damage = damage;
    }

    // Getters and setters
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

    public boolean isAccessible() {
        return accessible;
    }

}
