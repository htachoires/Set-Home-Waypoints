package com.dodgeman.shw.savedata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.mapper.CompoundMapper;
import com.dodgeman.shw.savedata.mapper.SetHomeAndWaypointsSavedDataMapper;
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

        return dataStorage.computeIfAbsent(this::load, SetHomeAndWaypointsSavedData::new, SetHomeWaypoints.MODID);
    }

    public SetHomeAndWaypointsSavedData load(CompoundTag tag) {
        return setHomeAndWaypointsSavedDataMapper.fromCompoundTag(tag);
    }
}
