package com.dodgeman.shw.saveddata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.saveddata.mappers.CompoundMapper;
import com.dodgeman.shw.saveddata.mappers.SetHomeAndWaypointsSavedDataMapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;

public class SetHomeWaypointsSavedDataFactory {

    private final CompoundMapper<SetHomeAndWaypointsSavedData> setHomeAndWaypointsSavedDataMapper;

    public SetHomeWaypointsSavedDataFactory() {
        this.setHomeAndWaypointsSavedDataMapper = new SetHomeAndWaypointsSavedDataMapper();
    }

    public SetHomeAndWaypointsSavedData createAndLoad() {
        DimensionDataStorage dataStorage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();

        return dataStorage.computeIfAbsent(this::load, SetHomeAndWaypointsSavedData::new, SetHomeWaypoints.MOD_ID);
    }

    public SetHomeAndWaypointsSavedData load(CompoundTag tag) {
        return setHomeAndWaypointsSavedDataMapper.fromCompoundTag(tag);
    }
}
