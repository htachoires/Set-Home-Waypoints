package com.dodgeman.shw.saveddata.models;

public final class Waypoint {
    private final WaypointName name;
    private final Position position;

    public Waypoint(WaypointName name, Position position) {
        this.name = name;
        this.position = position;
    }

    public WaypointName name() {
        return name;
    }

    public Position position() {
        return position;
    }
}
