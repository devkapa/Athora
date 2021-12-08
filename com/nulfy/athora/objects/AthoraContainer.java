package com.nulfy.athora.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AthoraContainer extends AthoraObject {

    private final ArrayList<AthoraObject> contents;
    private final long maxMass;

    public AthoraContainer(String name, String type, boolean accessible, long mass, long damage, long maxMass, ArrayList<AthoraObject> contents) {
        super(name, type, accessible, mass, damage);
        this.contents = contents;
        this.maxMass = maxMass;
    }

    public ArrayList<AthoraObject> getContents() {
        return contents;
    }

    public long getMaxMass() {
        return maxMass;
    }

    @Override
    public long getDamage() {
        long n = super.getDamage();
        for(AthoraObject o : contents){
            n -= o.getMass();
        }
        return n;
    }

}
