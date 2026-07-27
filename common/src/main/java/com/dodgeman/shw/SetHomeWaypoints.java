package com.dodgeman.shw;

import com.dodgeman.shw.client.commands.HomeCommand;
import com.dodgeman.shw.client.commands.SetHomeCommand;
import com.dodgeman.shw.client.commands.ShwCommand;
import com.dodgeman.shw.client.commands.WaypointsCommand;
import com.dodgeman.shw.config.ShwConfig;
import com.dodgeman.shw.networking.ModMessage;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;

/**
 * Loader-agnostic entry point. Invoked from the Fabric ({@code ModInitializer}) and
 * Forge ({@code @Mod}) entry points. All registration goes through the Architectury API
 * so a single code path serves both loaders.
 */
public class SetHomeWaypoints {

    public static final String MOD_ID = "shw";

    public static void init() {
        // Load the per-world server config (<world>/serverconfig/shw-server.toml) when the server starts.
        LifecycleEvent.SERVER_STARTING.register(ShwConfig::load);

        // Register commands against the vanilla dispatcher provided by Architectury.
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            HomeCommand.register(dispatcher);
            SetHomeCommand.register(dispatcher);
            WaypointsCommand.register(dispatcher);
            ShwCommand.register(dispatcher);
        });

        // Networking (the single go-home client-to-server packet).
        ModMessage.register();
    }
}
