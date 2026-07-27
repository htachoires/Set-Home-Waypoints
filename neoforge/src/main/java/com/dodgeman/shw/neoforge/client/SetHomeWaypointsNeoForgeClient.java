package com.dodgeman.shw.neoforge.client;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.client.ShwClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SetHomeWaypoints.MOD_ID, dist = Dist.CLIENT)
public class SetHomeWaypointsNeoForgeClient {
    public SetHomeWaypointsNeoForgeClient() {
        ShwClient.init();
    }
}
