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

        return new PlayerHomeAndWaypoints(home, waypoints, homeCommandLastUse, waypointCommandLastUse, lastDeletedWaypoint);
    }

    @Override
    public CompoundTag toCompoundTag(PlayerHomeAndWaypoints playerHomeAndWaypoints) {
        CompoundTag tag = new CompoundTag();
        if (playerHomeAndWaypoints == null) return tag;

        CompoundTag waypointsTag = new CompoundTag();

        playerHomeAndWaypoints
                .getWaypoints()
                .forEach((name, waypoint) -> waypointsTag.put(name, waypointMapper.toCompoundTag(waypoint)));

        tag.put(HOME_KEY, homeMapper.toCompoundTag(playerHomeAndWaypoints.getHome()));
        tag.put(WAYPOINTS_KEY, waypointsTag);

        tag.putLong(HOME_COMMAND_LAST_USE_KEY, playerHomeAndWaypoints.getHomeCommandLastUse());
        tag.putLong(WAYPOINT_COMMAND_LAST_USE_KEY, playerHomeAndWaypoints.getWaypointCommandLastUse());

        tag.put(LAST_DELETED_WAYPOINT_KEY, waypointMapper.toCompoundTag(playerHomeAndWaypoints.getLastDeletedWaypoint()));

        return tag;
    }
}
