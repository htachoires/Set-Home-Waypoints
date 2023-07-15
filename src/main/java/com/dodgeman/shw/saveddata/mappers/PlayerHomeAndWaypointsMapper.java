package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class PlayerHomeAndWaypointsMapper implements CompoundMapper<PlayerHomeAndWaypoints> {

    private static final String HOME_KEY = "home";
    private static final String WAYPOINTS_KEY = "waypoints";
    private static final String HAS_ALREADY_SET_WAYPOINT_KEY = "hasAlreadySetWaypoint";
    private static final String HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY = "hasAlreadySetHomeInTheEnd";
    private static final String HAS_ALREADY_SET_HOME_IN_THE_END_KEY = "hasAlreadySetHomeInTheNether";
    public static final String LAST_EXECUTION_OF_HOME_COMMAND_KEY = "lastExecutionOfHomeCommand";
    public static final String LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY = "lastExecutionOfWaypointUseCommand";
    private static final String LAST_DELETED_WAYPOINT_KEY = "lastDeletedWaypoint";
    private static final String LAST_DELETED_WAYPOINT_AT_KEY = "lastDeletedWaypointAt";
    private static final String UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY = "undoInformationHasBeenShownAt";

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

        boolean hasAlreadySetWaypoint = tag.getBoolean(HAS_ALREADY_SET_WAYPOINT_KEY);
        boolean hasAlreadySetHomeInTheNether = tag.getBoolean(HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY);
        boolean hasAlreadySetHomeInTheEnd = tag.getBoolean(HAS_ALREADY_SET_HOME_IN_THE_END_KEY);

        long lastExecutionOfHomeCommand = tag.getLong(LAST_EXECUTION_OF_HOME_COMMAND_KEY);
        long lastExecutionOfWaypointUseCommand = tag.getLong(LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY);

        Waypoint lastDeletedWaypoint = waypointMapper.fromCompoundTag(tag.getCompound(LAST_DELETED_WAYPOINT_KEY));
        long lastDeletedWaypointAt = tag.getLong(LAST_DELETED_WAYPOINT_AT_KEY);

        long undoInformationHasBeenShownAt = tag.getLong(UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY);

        return new PlayerHomeAndWaypoints(
                home,
                waypoints,
                hasAlreadySetWaypoint,
                hasAlreadySetHomeInTheNether,
                hasAlreadySetHomeInTheEnd,
                lastExecutionOfHomeCommand,
                lastExecutionOfWaypointUseCommand,
                lastDeletedWaypoint,
                lastDeletedWaypointAt,
                undoInformationHasBeenShownAt
        );
    }

    @Override
    public CompoundTag toCompoundTag(PlayerHomeAndWaypoints playerHomeAndWaypoints) {
        CompoundTag tag = new CompoundTag();
        if (playerHomeAndWaypoints == null) return tag;

        CompoundTag waypointsTag = new CompoundTag();

        playerHomeAndWaypoints
                .getWaypoints()
                .forEach((waypointName, waypoint) -> waypointsTag.put(waypointName.value(), waypointMapper.toCompoundTag(waypoint)));

        tag.put(HOME_KEY, homeMapper.toCompoundTag(playerHomeAndWaypoints.getHome()));
        tag.put(WAYPOINTS_KEY, waypointsTag);

        tag.putLong(LAST_EXECUTION_OF_HOME_COMMAND_KEY, playerHomeAndWaypoints.getLastExecutionOfHomeCommand());
        tag.putLong(LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY, playerHomeAndWaypoints.getLastExecutionOfWaypointUseCommand());

        tag.putBoolean(HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY, playerHomeAndWaypoints.hasAlreadySetHomeInTheNether());
        tag.putBoolean(HAS_ALREADY_SET_HOME_IN_THE_END_KEY, playerHomeAndWaypoints.hasAlreadySetHomeInTheEnd());
        tag.putBoolean(HAS_ALREADY_SET_WAYPOINT_KEY, playerHomeAndWaypoints.hasAlreadySetWaypoint());
        tag.putLong(LAST_DELETED_WAYPOINT_AT_KEY, playerHomeAndWaypoints.getLastDeletedWaypointAt());
        tag.putLong(UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY, playerHomeAndWaypoints.getUndoInformationHasBeenShownAt());

        tag.put(LAST_DELETED_WAYPOINT_KEY, waypointMapper.toCompoundTag(playerHomeAndWaypoints.getLastDeletedWaypoint()));

        return tag;
    }
}
