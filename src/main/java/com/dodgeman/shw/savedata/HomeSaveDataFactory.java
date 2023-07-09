package com.dodgeman.shw.savedata;

import com.dodgeman.shw.SetHomeWaypoints;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class HomeSaveDataFactory {

    public static HomeSaveData instance() {
        DimensionDataStorage dataStorage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();

        return dataStorage.computeIfAbsent(HomeSaveDataFactory::load, HomeSaveData::new, SetHomeWaypoints.MODID);
    }

    public static HomeSaveData load(CompoundTag compoundTag) {
        HomeSaveData homeSaveData = new HomeSaveData();

        CompoundTag rawHomeSaveData = compoundTag.getCompound(SetHomeWaypoints.MODID);

        rawHomeSaveData
                .getAllKeys()
                .forEach(uuid -> homeSaveData.setHomePositionForPlayer(
                        UUID.fromString(uuid),
                        HomePositionMapper.fromCompoundTag(rawHomeSaveData.getCompound(uuid))));

        return homeSaveData;
    }
}
