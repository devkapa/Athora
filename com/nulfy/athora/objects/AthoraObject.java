package com.nulfy.athora.objects;

public class AthoraObject {

    private final String name;
    private final String type;
    private boolean accessible;

    public AthoraObject(String name, String type, boolean accessible) {
        this.name = name;
        this.type = type;
        this.accessible = accessible;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void accessible(boolean bool) { accessible = bool; }

}
