package com.dodgeman.shw.savedata.mapper;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.savedata.model.PlayerHomeAndWaypoints;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.UUID;

public class SetHomeAndWaypointsSavedDataMapper implements CompoundMapper<SetHomeAndWaypointsSavedData> {
    private final CompoundMapper<PlayerHomeAndWaypoints> playerHomeAndWaypointsMapper;

    public SetHomeAndWaypointsSavedDataMapper() {
        this.playerHomeAndWaypointsMapper = new PlayerHomeAndWaypointsMapper();
    }

    @Override
    public SetHomeAndWaypointsSavedData fromCompoundTag(CompoundTag tag) {
        CompoundTag rawPlayerHomeAndWaypoints = tag.getCompound(SetHomeWaypoints.MODID);

        HashMap<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints = new HashMap<>();

        rawPlayerHomeAndWaypoints
                .getAllKeys()
                .forEach(uuid -> playersHomeAndWaypoints.put(
                        UUID.fromString(uuid),
                        playerHomeAndWaypointsMapper.fromCompoundTag(rawPlayerHomeAndWaypoints.getCompound(uuid))));

        return new SetHomeAndWaypointsSavedData(playersHomeAndWaypoints);
    }

    @Override
    public CompoundTag toCompoundTag(SetHomeAndWaypointsSavedData savedData) {
        CompoundTag tag = new CompoundTag();

        savedData.getPlayersHomeAndWaypoints().forEach((uuid, playerHomeAndWaypoints) -> tag.put(uuid.toString(), playerHomeAndWaypointsMapper.toCompoundTag(playerHomeAndWaypoints)));

        return tag;
    }
}
