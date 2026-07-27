package com.dodgeman.shw.fabric.client;

import com.dodgeman.shw.client.ShwClient;
import net.fabricmc.api.ClientModInitializer;

public class SetHomeWaypointsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ShwClient.init();
    }
}
