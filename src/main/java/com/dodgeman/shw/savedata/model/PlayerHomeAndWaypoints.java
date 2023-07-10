package com.dodgeman.shw.savedata.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PlayerHomeAndWaypoints {

    private Home home;
    private long homeCommandLastUse;

    private final HashMap<String, Waypoint> waypoints;
    private long waypointCommandLastUse;

    private Waypoint lastDeletedWaypoint;

    public PlayerHomeAndWaypoints() {
        waypoints = new HashMap<>();
    }

    public PlayerHomeAndWaypoints(Home home, List<Waypoint> waypoints, long homeCommandLastUse, long waypointCommandLastUse, Waypoint lastDeletedWaypoint) {
        this.home = home;
        this.homeCommandLastUse = homeCommandLastUse;
        this.lastDeletedWaypoint = lastDeletedWaypoint;
        this.waypoints = new HashMap<>();
        this.waypointCommandLastUse = waypointCommandLastUse;

        waypoints.forEach(waypoint -> this.waypoints.put(waypoint.name(), waypoint));
    }

    public void playerUsedHomeCommand() {
        homeCommandLastUse = new Date().getTime();
    }

    public long getHomeCommandLastUse() {
        return homeCommandLastUse;
    }

    public void playerUsedWaypointCommand() {
        waypointCommandLastUse = new Date().getTime();
        clearLastDeletedWaypoint();
    }

    public long getWaypointCommandLastUse() {
        return waypointCommandLastUse;
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
        clearLastDeletedWaypoint();
    }

    public Waypoint getWaypoint(String waypointName) {
        return waypoints.get(waypointName);
    }

    public void removeWaypoint(String waypointName) {
        lastDeletedWaypoint = waypoints.remove(waypointName);
    }

    public void clearWaypointOfPlayer() {
        waypoints.clear();
        clearLastDeletedWaypoint();
    }

    public void clearLastDeletedWaypoint() {
        lastDeletedWaypoint = null;
    }

    public Waypoint getLastDeletedWaypoint() {
        return lastDeletedWaypoint;
    }

    public int getNumberOfWaypoints() {
        return waypoints.values().size();
    }

    public boolean hasWaypointNamed(String waypointName) {
        return waypoints.containsKey(waypointName);
    }

    public void undoLastDeletedWaypoint() {
        waypoints.put(lastDeletedWaypoint.name(), lastDeletedWaypoint);
        clearLastDeletedWaypoint();
    }

    public boolean hasLastDeletedWaypoint() {
        return lastDeletedWaypoint != null;
    }
}
