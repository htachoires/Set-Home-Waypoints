package com.dodgeman.shw.saveddata.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PlayerHomeAndWaypoints {

    private Home home;
    private final HashMap<WaypointName, Waypoint> waypoints;
    private boolean hasAlreadySetWaypoint = false;
    private boolean hasAlreadySetHomeInTheNether;
    private boolean hasAlreadySetHomeInTheEnd;
    private long lastExecutionOfHomeCommand;
    private long lastExecutionOfWaypointUseCommand;
    private Waypoint lastDeletedWaypoint;
    private long lastDeletedWaypointAt = 0;
    private long undoInformationHasBeenShownAt = 0;
    private int removeWaypointSuccessMessageIndex = 1;

    public PlayerHomeAndWaypoints() {
        waypoints = new HashMap<>();
    }

    public PlayerHomeAndWaypoints(
            Home home,
            List<Waypoint> waypoints,
            boolean hasAlreadySetWaypoint,
            boolean hasAlreadySetHomeInTheNether,
            boolean hasAlreadySetHomeInTheEnd,
            long lastExecutionOfHomeCommand,
            long lastExecutionOfWaypointUseCommand,
            Waypoint lastDeletedWaypoint,
            long lastDeletedWaypointAt,
            long undoInformationHasBeenShowAt,
            int removeWaypointSuccessMessageIndex
    ) {
        this.home = home;
        this.waypoints = new HashMap<>();
        this.hasAlreadySetWaypoint = hasAlreadySetWaypoint;
        this.hasAlreadySetHomeInTheNether = hasAlreadySetHomeInTheNether;
        this.hasAlreadySetHomeInTheEnd = hasAlreadySetHomeInTheEnd;
        this.lastExecutionOfHomeCommand = lastExecutionOfHomeCommand;
        this.lastExecutionOfWaypointUseCommand = lastExecutionOfWaypointUseCommand;
        this.lastDeletedWaypoint = lastDeletedWaypoint;
        this.lastDeletedWaypointAt = lastDeletedWaypointAt;
        this.undoInformationHasBeenShownAt = undoInformationHasBeenShowAt;
        this.removeWaypointSuccessMessageIndex = removeWaypointSuccessMessageIndex;

        waypoints.forEach(waypoint -> this.waypoints.put(waypoint.name(), waypoint));
    }

    public void setNewHome(Home newHome) {
        home = newHome;

        if (home.position().isInTheNether()) {
            hasAlreadySetHomeInTheNether = true;
        }

        if (home.position().isInTheEnd()) {
            hasAlreadySetHomeInTheEnd = true;
        }
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.name(), waypoint);
        hasAlreadySetWaypoint = true;
        clearLastDeletedWaypoint();
    }

    public boolean hasWaypointNamed(WaypointName name) {
        return waypoints.containsKey(name);
    }

    public Waypoint getWaypointByName(WaypointName name) {
        return waypoints.get(name);
    }

    public void removeWaypoint(WaypointName name) {
        lastDeletedWaypoint = waypoints.remove(name);
        lastDeletedWaypointAt = new Date().getTime();
    }

    public void undoLastDeletedWaypoint() {
        waypoints.put(lastDeletedWaypoint.name(), lastDeletedWaypoint);
        clearLastDeletedWaypoint();
    }

    public void undoInformationHasBeenShown() {
        undoInformationHasBeenShownAt = new Date().getTime();
    }

    public void clearWaypoints() {
        waypoints.clear();
        clearLastDeletedWaypoint();
    }

    public void waypointUseCommandHasBeenExecuted() {
        lastExecutionOfWaypointUseCommand = new Date().getTime();
        clearLastDeletedWaypoint();
    }

    public void clearLastDeletedWaypoint() {
        lastDeletedWaypoint = null;
    }

    public long elapsedTimeOfWaypointUseCommandExecution() {
        return new Date().getTime() - lastExecutionOfWaypointUseCommand;
    }

    public void homeCommandHasBeenExecuted() {
        lastExecutionOfHomeCommand = new Date().getTime();
    }

    public long elapsedTimeOfLastHomeCommandExecution() {
        return new Date().getTime() - lastExecutionOfHomeCommand;
    }

    public void updateRemoveWaypointSuccessMessageIndex(int nbRemoveWaypointSuccessMessage) {
        if (removeWaypointSuccessMessageIndex < nbRemoveWaypointSuccessMessage) {
            removeWaypointSuccessMessageIndex++;
            return;
        }

        removeWaypointSuccessMessageIndex = 1;
    }

    public Home getHome() {
        return home;
    }

    public HashMap<WaypointName, Waypoint> getWaypoints() {
        return waypoints;
    }

    public int getNbOfWaypoints() {
        return waypoints.values().size();
    }

    public boolean hasAlreadySetWaypoint() {
        return hasAlreadySetWaypoint;
    }

    public boolean hasAlreadySetHomeInTheNether() {
        return hasAlreadySetHomeInTheNether;
    }

    public boolean hasAlreadySetHomeInTheEnd() {
        return hasAlreadySetHomeInTheEnd;
    }

    public long getLastExecutionOfHomeCommand() {
        return lastExecutionOfHomeCommand;
    }

    public long getLastExecutionOfWaypointUseCommand() {
        return lastExecutionOfWaypointUseCommand;
    }

    public Waypoint getLastDeletedWaypoint() {
        return lastDeletedWaypoint;
    }

    public boolean hasLastDeletedWaypoint() {
        return lastDeletedWaypoint != null;
    }

    public List<WaypointName> getWaypointsName() {
        return waypoints.keySet().stream().toList();
    }

    public long getLastDeletedWaypointAt() {
        return lastDeletedWaypointAt;
    }

    public long getUndoInformationHasBeenShownAt() {
        return undoInformationHasBeenShownAt;
    }

    public int getRemoveWaypointSuccessMessageIndex() {
        return removeWaypointSuccessMessageIndex;
    }
}
