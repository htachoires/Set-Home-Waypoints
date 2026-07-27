package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mappers.CompoundMapper;
import com.dodgeman.shw.saveddata.mappers.SetHomeAndWaypointsSavedDataMapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SetHomeWaypointsSavedDataFactory {

    private final CompoundMapper<SetHomeAndWaypointsSavedData> setHomeAndWaypointsSavedDataMapper;

    public SetHomeWaypointsSavedDataFactory() {
        this.setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    public SetHomeAndWaypointsSavedData createAndLoad(MinecraftServer server) {
        DimensionDataStorage dataStorage = server.overworld().getDataStorage();

        return dataStorage.computeIfAbsent(this::load, SetHomeAndWaypointsSavedData::new, SetHomeWaypoints.MOD_ID);
    }

    public SetHomeAndWaypointsSavedData load(CompoundTag tag) {
        return setHomeAndWaypointsSavedDataMapper.fromCompoundTag(tag);
    }
}
