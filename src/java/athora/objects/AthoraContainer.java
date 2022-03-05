package athora.objects;

import java.util.ArrayList;

public class AthoraContainer extends AthoraInvItem {

    private final ArrayList<AthoraInvItem> contents;
    private final int maxMass;

    public AthoraContainer(String name, boolean accessible, int mass, int damage, int maxMass, ArrayList<AthoraInvItem> contents) {
        super(name, accessible, mass, damage);
        this.contents = contents;
        this.maxMass = maxMass;
    }

    public ArrayList<AthoraInvItem> getContents() {
        return contents;
    }

    public int getMaxMass() {
        return maxMass;
    }

    @Override
    public int getDamage() {
        int n = super.getDamage();
        for(AthoraInvItem o : contents){
            n -= o.getMass();
        }
        return n;
    }

    @Override
    public int getMass() {
        int n = super.getMass();
        for(AthoraInvItem o : contents){
            n += o.getMass();
        }
        return n;
    }

}
