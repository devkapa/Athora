package athora.objects;

import java.util.ArrayList;

public class AthoraContainer extends AthoraObject {

    private final ArrayList<AthoraObject> contents;
    private final int maxMass;

    // Container constructor, to be used when initiating a new enemy object.
    public AthoraContainer(String name, boolean accessible, int mass, int damage, int maxMass, ArrayList<AthoraObject> contents) {
        super(name, accessible, mass, damage);
        this.contents = contents;
        this.maxMass = maxMass;
    }

    // Return an ArrayList of all objects within the container
    public ArrayList<AthoraObject> getContents() {
        return contents;
    }

    // Return an integer value of the maximum mass that can be stored in the container
    public int getMaxMass() {
        return maxMass;
    }

    @Override
    // Return an integer value of the damage that the container does.
    // This overrides the AthoraObject getDamage() as it accounts for the
    // mass of the objects stored inside the container as well as its own
    public int getDamage() {
        int n = super.getDamage();
        for(AthoraObject o : contents){
            n += o.getMass();
        }
        return n;
    }

    @Override
    // Return an integer value of the mass of the container.
    // This overrides the AthoraObject getMass() as it accounts for the
    // mass of the objects stored inside the container as well as its own
    public int getMass() {
        int n = super.getMass();
        for(AthoraObject o : contents){
            n += o.getMass();
        }
        return n;
    }

}
