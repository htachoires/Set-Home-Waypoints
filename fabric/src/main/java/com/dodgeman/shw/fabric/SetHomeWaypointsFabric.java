package com.dodgeman.shw.fabric;

import com.dodgeman.shw.SetHomeWaypoints;
import net.fabricmc.api.ModInitializer;

public class SetHomeWaypointsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SetHomeWaypoints.init();
    }
}
