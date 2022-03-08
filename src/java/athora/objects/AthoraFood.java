package athora.objects;

public class AthoraFood extends AthoraObject {

    private final int saturation;

    public AthoraFood(String name, boolean accessible, int mass, int damage, int saturation) {
        super(name, accessible, mass, damage);
        this.saturation = saturation;
    }

    public int getSaturation() {
        return saturation;
    }

}
