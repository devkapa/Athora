package athora.objects;

import java.util.ArrayList;

public class AthoraContainer extends AthoraObject {

    private final ArrayList<AthoraObject> contents;
    private final int maxMass;

    public AthoraContainer(String name, boolean accessible, int mass, int damage, int maxMass, ArrayList<AthoraObject> contents) {
        super(name, accessible, mass, damage);
        this.contents = contents;
        this.maxMass = maxMass;
    }

    public ArrayList<AthoraObject> getContents() {
        return contents;
    }

    public int getMaxMass() {
        return maxMass;
    }

    @Override
    public int getDamage() {
        int n = super.getDamage();
        for(AthoraObject o : contents){
            n += o.getMass();
        }
        return n;
    }

    @Override
    public int getMass() {
        int n = super.getMass();
        for(AthoraObject o : contents){
            n += o.getMass();
        }
        return n;
    }

}
