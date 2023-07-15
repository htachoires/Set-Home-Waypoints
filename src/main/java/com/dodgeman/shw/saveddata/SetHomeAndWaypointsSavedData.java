package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mappers.CompoundMapper;
import com.dodgeman.shw.saveddata.mappers.SetHomeAndWaypointsSavedDataMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
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

    public PlayerHomeAndWaypoints getPlayerHomeAndWaypoints(UUID playerUUID) {
        PlayerHomeAndWaypoints playerHomeAndWaypoints = playersHomeAndWaypoints.getOrDefault(playerUUID, new PlayerHomeAndWaypoints());

        this.playersHomeAndWaypoints.computeIfAbsent(playerUUID, uuid -> playerHomeAndWaypoints);

        return playerHomeAndWaypoints;
    }

    public Map<UUID, PlayerHomeAndWaypoints> getPlayersHomeAndWaypoints() {
        return playersHomeAndWaypoints;
    }
}