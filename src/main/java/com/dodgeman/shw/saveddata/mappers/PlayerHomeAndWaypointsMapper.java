package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerHomeAndWaypointsMapper implements CompoundMapper<PlayerHomeAndWaypoints> {

    private static final String HOME_KEY = "home";
    private static final String WAYPOINTS_KEY = "waypoints";
    private static final String HAS_ALREADY_SET_WAYPOINT_KEY = "hasAlreadySetWaypoint";
    private static final String HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY = "hasAlreadySetHomeInTheNether";
    private static final String HAS_ALREADY_SET_HOME_IN_THE_END_KEY = "hasAlreadySetHomeInTheEnd";
    public static final String LAST_EXECUTION_OF_HOME_COMMAND_KEY = "lastExecutionOfHomeCommand";
    public static final String LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY = "lastExecutionOfWaypointUseCommand";
    private static final String LAST_DELETED_WAYPOINT_KEY = "lastDeletedWaypoint";
    private static final String LAST_DELETED_WAYPOINT_AT_KEY = "lastDeletedWaypointAt";
    private static final String UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY = "undoInformationHasBeenShownAt";
    private static final String REMOVE_WAYPOINT_SUCCESS_MESSAGE_INDEX_KEY = "removeWaypointSuccessMessageIndex";

    private final CompoundMapper<Home> homeMapper;
    private final CompoundMapper<Waypoint> waypointMapper;

    public PlayerHomeAndWaypointsMapper() {
        homeMapper = new HomeMapper();
        waypointMapper = new WaypointMapper();
    }

    @Override
    public PlayerHomeAndWaypoints fromCompoundTag(NBTTagCompound tag) {
        Home home = homeMapper.fromCompoundTag(tag.getCompoundTag(HOME_KEY));

        NBTTagCompound waypointsTag = tag.getCompoundTag(WAYPOINTS_KEY);
        List<Waypoint> waypoints = waypointsTag
                .getKeySet()
                .stream()
                .map(waypointName -> waypointMapper.fromCompoundTag(waypointsTag.getCompoundTag(waypointName)))
                .collect(Collectors.toList());

        boolean hasAlreadySetWaypoint = tag.getBoolean(HAS_ALREADY_SET_WAYPOINT_KEY);
        boolean hasAlreadySetHomeInTheNether = tag.getBoolean(HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY);
        boolean hasAlreadySetHomeInTheEnd = tag.getBoolean(HAS_ALREADY_SET_HOME_IN_THE_END_KEY);

        long lastExecutionOfHomeCommand = tag.getLong(LAST_EXECUTION_OF_HOME_COMMAND_KEY);
        long lastExecutionOfWaypointUseCommand = tag.getLong(LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY);

        Waypoint lastDeletedWaypoint = waypointMapper.fromCompoundTag(tag.getCompoundTag(LAST_DELETED_WAYPOINT_KEY));
        long lastDeletedWaypointAt = tag.getLong(LAST_DELETED_WAYPOINT_AT_KEY);

        long undoInformationHasBeenShownAt = tag.getLong(UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY);
        int removeWaypointSuccessMessageIndex = tag.getInteger(REMOVE_WAYPOINT_SUCCESS_MESSAGE_INDEX_KEY);

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
                undoInformationHasBeenShownAt,
                removeWaypointSuccessMessageIndex
        );
    }

    @Override
    public NBTTagCompound toCompoundTag(PlayerHomeAndWaypoints playerHomeAndWaypoints) {
        NBTTagCompound tag = new NBTTagCompound();
        if (playerHomeAndWaypoints == null) return tag;

        NBTTagCompound waypointsTag = new NBTTagCompound();

        playerHomeAndWaypoints
                .getWaypoints()
                .forEach((waypointName, waypoint) -> waypointsTag.setTag(waypointName.value(), waypointMapper.toCompoundTag(waypoint)));

        tag.setTag(HOME_KEY, homeMapper.toCompoundTag(playerHomeAndWaypoints.getHome()));
        tag.setTag(WAYPOINTS_KEY, waypointsTag);

        tag.setBoolean(HAS_ALREADY_SET_WAYPOINT_KEY, playerHomeAndWaypoints.hasAlreadySetWaypoint());
        tag.setBoolean(HAS_ALREADY_SET_HOME_IN_THE_NETHER_KEY, playerHomeAndWaypoints.hasAlreadySetHomeInTheNether());
        tag.setBoolean(HAS_ALREADY_SET_HOME_IN_THE_END_KEY, playerHomeAndWaypoints.hasAlreadySetHomeInTheEnd());

        tag.setLong(LAST_EXECUTION_OF_HOME_COMMAND_KEY, playerHomeAndWaypoints.getLastExecutionOfHomeCommand());
        tag.setLong(LAST_EXECUTION_OF_WAYPOINT_USE_COMMAND_KEY, playerHomeAndWaypoints.getLastExecutionOfWaypointUseCommand());

        tag.setLong(LAST_DELETED_WAYPOINT_AT_KEY, playerHomeAndWaypoints.getLastDeletedWaypointAt());
        tag.setTag(LAST_DELETED_WAYPOINT_KEY, waypointMapper.toCompoundTag(playerHomeAndWaypoints.getLastDeletedWaypoint()));

        tag.setLong(UNDO_INFORMATION_HAS_BEEN_SHOWN_AT_KEY, playerHomeAndWaypoints.getUndoInformationHasBeenShownAt());
        tag.setLong(REMOVE_WAYPOINT_SUCCESS_MESSAGE_INDEX_KEY, playerHomeAndWaypoints.getRemoveWaypointSuccessMessageIndex());

        return tag;
    }
}
