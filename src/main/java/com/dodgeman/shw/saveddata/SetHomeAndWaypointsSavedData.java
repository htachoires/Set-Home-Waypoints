package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mapper.CompoundMapper;
import com.dodgeman.shw.saveddata.mapper.SetHomeAndWaypointsSavedDataMapper;
import com.dodgeman.shw.saveddata.model.Home;
import com.dodgeman.shw.saveddata.model.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.model.Waypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetHomeAndWaypointsSavedData extends SavedData {

    private final Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints;
    private final CompoundMapper<SetHomeAndWaypointsSavedData> setHomeAndWaypointsSavedDataMapper;

    public SetHomeAndWaypointsSavedData() {
        playersHomeAndWaypoints = new HashMap<>();
        setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    public SetHomeAndWaypointsSavedData(Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints) {
        this.playersHomeAndWaypoints = playersHomeAndWaypoints;
        setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.put(SetHomeWaypoints.MODID, setHomeAndWaypointsSavedDataMapper.toCompoundTag(this));
        return tag;
    }

    public Map<UUID, PlayerHomeAndWaypoints> getPlayersHomeAndWaypoints() {
        return playersHomeAndWaypoints;
    }

    public void setHomeForPlayer(UUID playerUUID, Home home) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = getPlayerHomeAndWaypoints(playerUUID);
        playerHomeAndWaypoints.setHome(home);

        playersHomeAndWaypoints.put(playerUUID, playerHomeAndWaypoints);
    }

    public Home getHomeOfPlayer(UUID playerUUID) {
        return getPlayerHomeAndWaypoints(playerUUID).getHome();
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

    public void clearWaypointOfPlayer(UUID playerUUID) {
        getPlayerHomeAndWaypoints(playerUUID).clearWaypointOfPlayer();
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

    public void undoLastDeletedWaypointOfPlayer(UUID playerUUID) {
        getPlayerHomeAndWaypoints(playerUUID).undoLastDeletedWaypoint();
    }

    public boolean playerHasLastDeletedWaypoint(UUID playerUUID) {
        return getPlayerHomeAndWaypoints(playerUUID).hasLastDeletedWaypoint();
    }

    public int getPlayerNumberOfWaypoints(UUID playerUUID) {
        return getPlayerHomeAndWaypoints(playerUUID).getNumberOfWaypoints();
    }

    public boolean playerHasWaypointNamed(UUID playerUUID, String waypointName) {
        return getPlayerHomeAndWaypoints(playerUUID).hasWaypointNamed(waypointName);
    }

    private PlayerHomeAndWaypoints getPlayerHomeAndWaypoints(UUID playerUUID) {
        return playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());
    }
}