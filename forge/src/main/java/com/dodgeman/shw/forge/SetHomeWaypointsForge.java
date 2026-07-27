package com.dodgeman.shw.forge;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.forge.client.SetHomeWaypointsForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(SetHomeWaypoints.MOD_ID)
public class SetHomeWaypointsForge {
    public SetHomeWaypointsForge() {
        SetHomeWaypoints.init();

        // Client-only setup (key mapping + per-tick polling). The method reference is
        // materialised only when running on the physical client, so the client bootstrap
        // class is never loaded on a dedicated server.
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> SetHomeWaypointsForgeClient::init);
    }
}
