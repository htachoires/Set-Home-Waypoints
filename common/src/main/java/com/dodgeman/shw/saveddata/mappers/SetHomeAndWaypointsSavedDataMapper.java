package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
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
        CompoundTag rawPlayerHomeAndWaypoints = tag.getCompound(SetHomeWaypoints.MOD_ID);

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
        if (savedData == null) return tag;

        savedData.getPlayersHomeAndWaypoints().forEach((uuid, playerHomeAndWaypoints) -> tag.put(uuid.toString(), playerHomeAndWaypointsMapper.toCompoundTag(playerHomeAndWaypoints)));

        return tag;
    }
}
