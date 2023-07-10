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
        PlayerHomeAndWaypoints playerHomeAndWaypoints = getPlayerHomeAndWaypoints(playerUUID);
        playerHomeAndWaypoints.setHome(home);

        playersHomeAndWaypoints.put(playerUUID, playerHomeAndWaypoints);
    }

    public Home getHomeOfPlayer(UUID playerUUID) {
        return playersHomeAndWaypoints.get(playerUUID).getHome();
    }

    public void addWaypointForPlayer(UUID playerUUID, Waypoint waypoint) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = getPlayerHomeAndWaypoints(playerUUID);
        playerHomeAndWaypoints.addWaypoints(waypoint);

        playersHomeAndWaypoints.put(playerUUID, playerHomeAndWaypoints);
    }

    public Waypoint getWaypointOfPlayer(UUID playerUUID, String waypointName) {
        return getPlayerHomeAndWaypoints(playerUUID).getWaypoint(waypointName);
    }

    public void removeWaypointOfPlayer(UUID playerUUID, String waypointName) {
        getPlayerHomeAndWaypoints(playerUUID).removeWaypoint(waypointName);
    }

    public Collection<Waypoint> getWaypointsOfPlayer(UUID playerUUID) {
        return getPlayerHomeAndWaypoints(playerUUID).getWaypoints().values();
    }

    public long getLastUseHomeCommandOfPlayer(UUID playerUUID) {

        return getPlayerHomeAndWaypoints(playerUUID).getHomeCommandLastUse();
    }

    public void playerUsedHomeCommand(UUID playerUUID) {
        getPlayerHomeAndWaypoints(playerUUID).playerUsedHomeCommand();
    }

    public long getLastUseWaypointCommandOfPlayer(UUID playerUUID) {
        return getPlayerHomeAndWaypoints(playerUUID).getWaypointCommandLastUse();
    }

    public void playerUsedWaypointCommand(UUID playerUUID) {
        getPlayerHomeAndWaypoints(playerUUID).playerUsedWaypointCommand();
    }

    private PlayerHomeAndWaypoints getPlayerHomeAndWaypoints(UUID playerUUID) {
        return playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
    }
}