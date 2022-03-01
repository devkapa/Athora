package athora.objects;

import java.util.ArrayList;

public class AthoraContainer extends AthoraInvItem {

    private final ArrayList<AthoraInvItem> contents;
    private final long maxMass;

    public AthoraContainer(String name, String type, boolean accessible, long mass, long damage, long maxMass, ArrayList<AthoraInvItem> contents) {
        super(name, type, accessible, mass, damage);
        this.contents = contents;
        this.maxMass = maxMass;
    }

    public ArrayList<AthoraInvItem> getContents() {
        return contents;
    }

    public long getMaxMass() {
        return maxMass;
    }

    @Override
    public long getDamage() {
        long n = super.getDamage();
        for(AthoraInvItem o : contents){
            n -= o.getMass();
        }
        return n;
    }

    @Override
    public long getMass() {
        long n = super.getMass();
        for(AthoraInvItem o : contents){
            n += o.getMass();
        }
        return n;
    }

}
