package com.dodgeman.shw.savedata.model;

import java.util.HashMap;
import java.util.List;

public class PlayerHomeAndWaypoints {

    private Home home;

    private final HashMap<String, Waypoint> waypoints;

    public PlayerHomeAndWaypoints() {
        waypoints = new HashMap<>();
    }

    public PlayerHomeAndWaypoints(Home home, List<Waypoint> waypoints) {
        this.home = home;
        this.waypoints = new HashMap<>();
        waypoints.forEach(waypoint -> this.waypoints.put(waypoint.name(), waypoint));
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public HashMap<String, Waypoint> getWaypoints() {
        return waypoints;
    }

    public void addWaypoints(Waypoint waypoint) {
        this.waypoints.put(waypoint.name(), waypoint);
    }

    public Waypoint getWaypoint(String waypointName) {
        return waypoints.get(waypointName);
    }

    public void removeWaypoint(String waypointName) {
        waypoints.remove(waypointName);
    }
}
