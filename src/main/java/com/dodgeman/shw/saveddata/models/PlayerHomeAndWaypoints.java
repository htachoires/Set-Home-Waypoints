package com.dodgeman.shw.saveddata.models;

import java.util.*;

public class PlayerHomeAndWaypoints {

    private Home home;
    private boolean hasAlreadySetAHomeInTheNether;
    private boolean hasAlreadySetAHomeInTheEnd;
    private long homeCommandLastUse;

    private final HashMap<String, Waypoint> waypoints;
    private long waypointCommandLastUse;

    private Waypoint lastDeletedWaypoint;

    private boolean isFirstWaypoint = true;
    private long lastTimeDeletedWaypoint = 0;
    private long undoInformationHasBeenShowAt = 0;

    public PlayerHomeAndWaypoints() {
        waypoints = new HashMap<>();
    }

    public PlayerHomeAndWaypoints(Home home, List<Waypoint> waypoints, long homeCommandLastUse, long waypointCommandLastUse, Waypoint lastDeletedWaypoint, boolean hasAlreadySetAHomeInTheNether, boolean hasAlreadySetAHomeInTheEnd, boolean isFirstWaypoint, long lastTimeDeletedWaypoint, long undoInformationHasBeenShowAt) {
        this.home = home;
        this.homeCommandLastUse = homeCommandLastUse;
        this.lastDeletedWaypoint = lastDeletedWaypoint;
        this.hasAlreadySetAHomeInTheNether = hasAlreadySetAHomeInTheNether;
        this.hasAlreadySetAHomeInTheEnd = hasAlreadySetAHomeInTheEnd;
        this.isFirstWaypoint = isFirstWaypoint;
        this.lastTimeDeletedWaypoint = lastTimeDeletedWaypoint;
        this.undoInformationHasBeenShowAt = undoInformationHasBeenShowAt;
        this.waypoints = new HashMap<>();
        this.waypointCommandLastUse = waypointCommandLastUse;

        waypoints.forEach(waypoint -> this.waypoints.put(waypoint.name(), waypoint));
    }

    public void homeCommandHasBeenExecuted() {
        homeCommandLastUse = new Date().getTime();
    }

    public long getHomeCommandLastUse() {
        return homeCommandLastUse;
    }

    public long elapsedTimeOfLastHomeCommandExecution() {
        return new Date().getTime() - homeCommandLastUse;
    }

    public long elapsedTimeOfLastWaypointUseCommandExecution() {
        return new Date().getTime() - waypointCommandLastUse;
    }

    public void useWaypointCommandHasBeenExecuted() {
        waypointCommandLastUse = new Date().getTime();
        clearLastDeletedWaypoint();
    }

    public long getWaypointCommandLastUse() {
        return waypointCommandLastUse;
    }

    public Home getCurrentHome() {
        return home;
    }

    public void setNewHome(Home newHome) {
        home = newHome;

        if (home.position().isInTheNether()) {
            hasAlreadySetAHomeInTheNether = true;
        }

        if (home.position().isInTheEnd()) {
            hasAlreadySetAHomeInTheEnd = true;
        }
    }

    public boolean hasAlreadySetAHomeInTheNether() {
        return hasAlreadySetAHomeInTheNether;
    }

    public boolean hasAlreadySetAHomeInTheEnd() {
        return hasAlreadySetAHomeInTheEnd;
    }

    public HashMap<String, Waypoint> getWaypoints() {
        return waypoints;
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.name(), waypoint);
        isFirstWaypoint = false;
        clearLastDeletedWaypoint();
    }

    public Waypoint getWaypoint(String waypointName) {
        return waypoints.get(waypointName);
    }

    public void removeWaypoint(String waypointName) {
        lastDeletedWaypoint = waypoints.remove(waypointName);
        lastTimeDeletedWaypoint = new Date().getTime();
    }

    public void clearWaypoints() {
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

    public List<String> getWaypointsName() {
        return waypoints.keySet().stream().toList();
    }

    public boolean isFirstWaypoint() {
        return isFirstWaypoint;
    }

    public long getLastTimeDeletedWaypoint() {
        return lastTimeDeletedWaypoint;
    }

    public void updateUndoInformationHasBeenShowAt() {
        undoInformationHasBeenShowAt = new Date().getTime();
    }

    public long getUndoInformationHasBeenShowAt() {
        return undoInformationHasBeenShowAt;
    }
}
