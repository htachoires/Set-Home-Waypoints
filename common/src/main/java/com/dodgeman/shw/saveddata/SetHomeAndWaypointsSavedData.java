package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mappers.SetHomeAndWaypointsSavedDataMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class SetHomeAndWaypointsSavedData extends SavedData {

    // Since 1.21.5 SavedData persistence is Codec-based (the save(CompoundTag, ...) override
    // and SavedData.Factory were removed). We adapt the existing CompoundTag mappers over
    // CompoundTag.CODEC so the on-disk structure (a MOD_ID-keyed compound) stays identical.
    public static final Codec<SetHomeAndWaypointsSavedData> CODEC = CompoundTag.CODEC.xmap(
            tag -> new SetHomeAndWaypointsSavedDataMapper().fromCompoundTag(tag),
            data -> {
                CompoundTag tag = new CompoundTag();
                tag.put(SetHomeWaypoints.MOD_ID, new SetHomeAndWaypointsSavedDataMapper().toCompoundTag(data));
                return tag;
            });

    public static final SavedDataType<SetHomeAndWaypointsSavedData> TYPE =
            new SavedDataType<SetHomeAndWaypointsSavedData>(SetHomeWaypoints.MOD_ID, () -> new SetHomeAndWaypointsSavedData(), CODEC, null);

    private final Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints;

    public SetHomeAndWaypointsSavedData() {
        playersHomeAndWaypoints = new HashMap<>();
    }

    public SetHomeAndWaypointsSavedData(Map<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints) {
        this.playersHomeAndWaypoints = playersHomeAndWaypoints;
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
