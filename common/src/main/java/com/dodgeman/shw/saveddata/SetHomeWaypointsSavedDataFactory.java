package com.dodgeman.shw.saveddata;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.SavedDataStorage;

public class SetHomeWaypointsSavedDataFactory {

    public SetHomeAndWaypointsSavedData createAndLoad(MinecraftServer server) {
        // DimensionDataStorage was renamed to SavedDataStorage in MC 26.x.
        SavedDataStorage dataStorage = server.overworld().getDataStorage();

        return dataStorage.computeIfAbsent(SetHomeAndWaypointsSavedData.TYPE);
    }
}
