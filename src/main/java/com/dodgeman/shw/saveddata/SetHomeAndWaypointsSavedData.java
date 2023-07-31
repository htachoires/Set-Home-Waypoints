package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mappers.CompoundMapper;
import com.dodgeman.shw.saveddata.mappers.SetHomeAndWaypointsSavedDataMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetHomeAndWaypointsSavedData extends WorldSavedData {

    private Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints;
    private final CompoundMapper<SetHomeAndWaypointsSavedData> setHomeAndWaypointsSavedDataMapper;
    public static final String WORLD_SAVED_DATA_NAME = SetHomeWaypoints.MOD_ID;

    public SetHomeAndWaypointsSavedData(Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints) {
        super(WORLD_SAVED_DATA_NAME);
        this.playersHomeAndWaypoints = playersHomeAndWaypoints;
        setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    public SetHomeAndWaypointsSavedData(String name) {
        super(name);
        playersHomeAndWaypoints = new HashMap<>();
        setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        playersHomeAndWaypoints = setHomeAndWaypointsSavedDataMapper.fromCompoundTag(tag).playersHomeAndWaypoints;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag(SetHomeWaypoints.MOD_ID, setHomeAndWaypointsSavedDataMapper.toCompoundTag(this));
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
