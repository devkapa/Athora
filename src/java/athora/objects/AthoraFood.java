package athora.objects;

public class AthoraFood extends AthoraObject {

    private final int saturation;

    // Food constructor, to be used when initiating a new food object.
    public AthoraFood(String name, boolean accessible, int mass, int damage, int saturation) {
        super(name, accessible, mass, damage);
        this.saturation = saturation;
    }

    // Return an integer value of the food's saturation
    public int getSaturation() {
        return saturation;
    }

}
