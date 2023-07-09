package com.dodgeman.shw.savedata.mapper;

import com.dodgeman.shw.savedata.model.Home;
import com.dodgeman.shw.savedata.model.PlayerHomeAndWaypoints;
import com.dodgeman.shw.savedata.model.Waypoint;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class PlayerHomeAndWaypointsMapper implements CompoundMapper<PlayerHomeAndWaypoints> {

    private static final String HOME_KEY = "home";
    private static final String WAYPOINTS_KEY = "waypoints";

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

        return new PlayerHomeAndWaypoints(home, waypoints);
    }

    @Override
    public CompoundTag toCompoundTag(PlayerHomeAndWaypoints playerHomeAndWaypoints) {
        CompoundTag tag = new CompoundTag();
        CompoundTag waypointsTag = new CompoundTag();

        playerHomeAndWaypoints
                .getWaypoints()
                .forEach((name, waypoint) -> waypointsTag.put(name, waypointMapper.toCompoundTag(waypoint)));

        tag.put(HOME_KEY, homeMapper.toCompoundTag(playerHomeAndWaypoints.getHome()));
        tag.put(WAYPOINTS_KEY, waypointsTag);

        return tag;
    }
}
