package com.dodgeman.shw.savedata;

import com.dodgeman.shw.SetHomeWaypoints;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeSaveData extends SavedData {

    private Map<UUID, HomePosition> playersHomePosition;

    public HomeSaveData() {
        playersHomePosition = new HashMap<>();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        CompoundTag playersHomePositionTag = new CompoundTag();

        playersHomePosition.forEach((uuid, homePosition) -> playersHomePositionTag.put(uuid.toString(), homePosition.toCompoundTag()));

        tag.put(SetHomeWaypoints.MODID, playersHomePositionTag);

        return tag;
    }

    public void setHomePositionForPlayer(UUID playerUUID, HomePosition homePosition) {
        playersHomePosition.put(playerUUID, homePosition);
    }

    public HomePosition getHomePositionByUUID(UUID playerUUID) {
        return playersHomePosition.get(playerUUID);
    }
}