package com.dodgeman.shw.saveddata;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SetHomeWaypointsSavedDataFactory {

    public SetHomeAndWaypointsSavedData createAndLoad(MinecraftServer server) {
        DimensionDataStorage dataStorage = server.overworld().getDataStorage();

        return dataStorage.computeIfAbsent(SetHomeAndWaypointsSavedData.TYPE);
    }
}
