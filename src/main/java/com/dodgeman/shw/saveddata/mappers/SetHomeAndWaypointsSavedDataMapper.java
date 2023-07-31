package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.UUID;

public class SetHomeAndWaypointsSavedDataMapper implements CompoundMapper<SetHomeAndWaypointsSavedData> {
    private final CompoundMapper<PlayerHomeAndWaypoints> playerHomeAndWaypointsMapper;

    public SetHomeAndWaypointsSavedDataMapper() {
        this.playerHomeAndWaypointsMapper = new PlayerHomeAndWaypointsMapper();
    }

    @Override
    public SetHomeAndWaypointsSavedData fromCompoundTag(NBTTagCompound tag) {
        NBTTagCompound rawPlayerHomeAndWaypoints = tag.getCompoundTag(SetHomeWaypoints.MOD_ID);

        HashMap<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints = new HashMap<>();

        rawPlayerHomeAndWaypoints
                .getKeySet()
                .forEach(uuid -> playersHomeAndWaypoints.put(
                        UUID.fromString(uuid),
                        playerHomeAndWaypointsMapper.fromCompoundTag(rawPlayerHomeAndWaypoints.getCompoundTag(uuid))));

        return new SetHomeAndWaypointsSavedData(playersHomeAndWaypoints);
    }

    @Override
    public NBTTagCompound toCompoundTag(SetHomeAndWaypointsSavedData savedData) {
        NBTTagCompound tag = new NBTTagCompound();
        if (savedData == null) return tag;

        savedData.getPlayersHomeAndWaypoints().forEach((uuid, playerHomeAndWaypoints) -> tag.setTag(uuid.toString(), playerHomeAndWaypointsMapper.toCompoundTag(playerHomeAndWaypoints)));

        return tag;
    }
}
