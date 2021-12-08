package com.nulfy.athora.objects;

public class AthoraFood extends AthoraObject {

    private final long saturation;

    public AthoraFood(String name, String type, boolean accessible, long mass, long damage, long saturation) {
        super(name, type, accessible, mass, damage);
        this.saturation = saturation;
    }

    public long getSaturation() {
        return saturation;
    }

}
