package com.nulfy.athora.objects;

public class AthoraObject {

    private final long id;
    private final String name;
    private final String type;
    private final boolean accessible;

    public AthoraObject(long id, String name, String type, boolean accessible) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.accessible = accessible;
    }

    public long getId() {
        return id;
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
}
