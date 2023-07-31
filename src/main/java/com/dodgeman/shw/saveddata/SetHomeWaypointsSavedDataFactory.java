package com.dodgeman.shw.saveddata;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapStorage;

import static com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData.WORLD_SAVED_DATA_NAME;

public class SetHomeWaypointsSavedDataFactory {
    public SetHomeAndWaypointsSavedData createAndLoad(MinecraftServer server) {
        MapStorage storage = server.getWorld(0).getMapStorage();
        SetHomeAndWaypointsSavedData instance = (SetHomeAndWaypointsSavedData) storage.getOrLoadData(SetHomeAndWaypointsSavedData.class, WORLD_SAVED_DATA_NAME);

        if (instance == null) {
            instance = new SetHomeAndWaypointsSavedData(WORLD_SAVED_DATA_NAME);
            storage.setData(WORLD_SAVED_DATA_NAME, instance);
        }

        return instance;
    }
}
