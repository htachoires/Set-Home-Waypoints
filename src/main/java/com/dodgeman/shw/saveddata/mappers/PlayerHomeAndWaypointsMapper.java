package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class PlayerHomeAndWaypointsMapper implements CompoundMapper<PlayerHomeAndWaypoints> {

    private static final String HOME_KEY = "home";
    private static final String WAYPOINTS_KEY = "waypoints";
    public static final String HOME_COMMAND_LAST_USE_KEY = "homeCommandLastUse";
    public static final String WAYPOINT_COMMAND_LAST_USE_KEY = "waypointCommandLastUse";
    private static final String LAST_DELETED_WAYPOINT_KEY = "lastDeletedWaypoint";
    private static final String HAS_ALREADY_SET_A_HOME_IN_THE_END_KEY = "hasAlreadySetAHomeInTheEnd";
    private static final String HAS_ALREADY_SET_A_HOME_IN_THE_NETHER_KEY = "hasAlreadySetAHomeInTheNether";
    private static final String IS_FIRST_WAYPOINT_KEY = "isFirstWaypoint";
    private static final String LAST_TIME_DELETED_WAYPOINT_KEY = "lastTimeDeletedWaypoint";
    private static final String UNDO_INFORMATION_HAS_BEEN_SHOW_AT_KEY = "undoInformationHasBeenShowAt";

    private final CompoundMapper<Home> homeMapper;
    private final CompoundMapper<Waypoint> waypointMapper;

    public PlayerHomeAndWaypointsMapper() {
        homeMapper = new HomeMapper();
        waypointMapper = new WaypointMapper();
    }

    @Override
    public PlayerHomeAndWaypoints fromCompoundTag(CompoundTag tag) {
        Home home = homeMapper.fromCompoundTag(tag.getCompound(HOME_KEY));

        CompoundTag waypointsTag = tag.getCompound(WAYPOINTS_KEY);
        List<Waypoint> waypoints = waypointsTag
                .getAllKeys()
                .stream()
                .map(waypointName -> waypointMapper.fromCompoundTag(waypointsTag.getCompound(waypointName)))
                .toList();

        long homeCommandLastUse = tag.getLong(HOME_COMMAND_LAST_USE_KEY);
        long waypointCommandLastUse = tag.getLong(WAYPOINT_COMMAND_LAST_USE_KEY);

        Waypoint lastDeletedWaypoint = waypointMapper.fromCompoundTag(tag.getCompound(LAST_DELETED_WAYPOINT_KEY));

        boolean hasAlreadySetAHomeInTheNether = tag.getBoolean(HAS_ALREADY_SET_A_HOME_IN_THE_NETHER_KEY);
        boolean hasAlreadySetAHomeInTheEnd = tag.getBoolean(HAS_ALREADY_SET_A_HOME_IN_THE_END_KEY);
        boolean isFirstWaypoint = tag.getBoolean(IS_FIRST_WAYPOINT_KEY);
        long lastTimeDeletedWaypoint = tag.getLong(LAST_TIME_DELETED_WAYPOINT_KEY);
        long undoInformationHasBeenShowAt = tag.getLong(UNDO_INFORMATION_HAS_BEEN_SHOW_AT_KEY);

        return new PlayerHomeAndWaypoints(home, waypoints, homeCommandLastUse, waypointCommandLastUse, lastDeletedWaypoint, hasAlreadySetAHomeInTheNether, hasAlreadySetAHomeInTheEnd, isFirstWaypoint, lastTimeDeletedWaypoint, undoInformationHasBeenShowAt);
    }

    @Override
    public CompoundTag toCompoundTag(PlayerHomeAndWaypoints playerHomeAndWaypoints) {
        CompoundTag tag = new CompoundTag();
        if (playerHomeAndWaypoints == null) return tag;

        CompoundTag waypointsTag = new CompoundTag();

        playerHomeAndWaypoints
                .getWaypoints()
                .forEach((name, waypoint) -> waypointsTag.put(name, waypointMapper.toCompoundTag(waypoint)));

        tag.put(HOME_KEY, homeMapper.toCompoundTag(playerHomeAndWaypoints.getCurrentHome()));
        tag.put(WAYPOINTS_KEY, waypointsTag);

        tag.putLong(HOME_COMMAND_LAST_USE_KEY, playerHomeAndWaypoints.getHomeCommandLastUse());
        tag.putLong(WAYPOINT_COMMAND_LAST_USE_KEY, playerHomeAndWaypoints.getWaypointCommandLastUse());

        tag.putBoolean(HAS_ALREADY_SET_A_HOME_IN_THE_NETHER_KEY, playerHomeAndWaypoints.hasAlreadySetAHomeInTheNether());
        tag.putBoolean(HAS_ALREADY_SET_A_HOME_IN_THE_END_KEY, playerHomeAndWaypoints.hasAlreadySetAHomeInTheEnd());
        tag.putBoolean(IS_FIRST_WAYPOINT_KEY, playerHomeAndWaypoints.isFirstWaypoint());
        tag.putLong(LAST_TIME_DELETED_WAYPOINT_KEY, playerHomeAndWaypoints.getLastTimeDeletedWaypoint());
        tag.putLong(UNDO_INFORMATION_HAS_BEEN_SHOW_AT_KEY, playerHomeAndWaypoints.getUndoInformationHasBeenShowAt());

        tag.put(LAST_DELETED_WAYPOINT_KEY, waypointMapper.toCompoundTag(playerHomeAndWaypoints.getLastDeletedWaypoint()));

        return tag;
    }
}
