package com.dodgeman.shw.savedata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.mapper.CompoundMapper;
import com.dodgeman.shw.savedata.mapper.PlayerHomeAndWaypointsMapper;
import com.dodgeman.shw.savedata.model.Home;
import com.dodgeman.shw.savedata.model.PlayerHomeAndWaypoints;
import com.dodgeman.shw.savedata.model.Waypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetHomeAndWaypointsSavedData extends SavedData {

    private final Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints;
    private final CompoundMapper<PlayerHomeAndWaypoints> playersHomeAndWaypointsMapper;

    public SetHomeAndWaypointsSavedData() {
        playersHomeAndWaypoints = new HashMap<>();
        playersHomeAndWaypointsMapper = new PlayerHomeAndWaypointsMapper();
    }

    public SetHomeAndWaypointsSavedData(Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints) {
        this.playersHomeAndWaypoints = playersHomeAndWaypoints;
        playersHomeAndWaypointsMapper = new PlayerHomeAndWaypointsMapper();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        CompoundTag playersHomeAndWaypointsTag = new CompoundTag();

        playersHomeAndWaypoints.forEach((uuid, playerHomeAndWaypoints) -> playersHomeAndWaypointsTag.put(uuid.toString(), playersHomeAndWaypointsMapper.toCompoundTag(playerHomeAndWaypoints)));

        tag.put(SetHomeWaypoints.MODID, playersHomeAndWaypointsTag);

        return tag;
    }

    public void setHomeForPlayer(UUID playerUUID, Home home) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
        playerHomeAndWaypoints.setHome(home);

        playersHomeAndWaypoints.put(playerUUID, playerHomeAndWaypoints);
    }

    public Home getHomeOfPlayer(UUID playerUUID) {
        return playersHomeAndWaypoints.get(playerUUID).getHome();
    }

    public void addWaypointForPlayer(UUID playerUUID, Waypoint waypoint) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
        playerHomeAndWaypoints.addWaypoints(waypoint);

        playersHomeAndWaypoints.put(playerUUID, playerHomeAndWaypoints);
    }

    public Waypoint getWaypointOfPlayer(UUID playerUUID, String waypointName) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
        return playerHomeAndWaypoints.getWaypoint(waypointName);
    }

    public void removeWaypointOfPlayer(UUID playerUUID, String waypointName) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
        playerHomeAndWaypoints.removeWaypoint(waypointName);
    }

    public Collection<Waypoint> getWaypointsOfPlayer(UUID playerUUID) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());

        return playerHomeAndWaypoints.getWaypoints().values();
    }
}