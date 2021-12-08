package com.nulfy.athora.objects;

import java.util.ArrayList;

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

    public void addToContents(AthoraObject obj){
        if(obj.getMass() + getMass() < getMaxMass()) contents.add(obj);
        else System.out.println(obj.getName() + " can't fit in the " + this.getName() + " because it is too heavy.");
    }

}
