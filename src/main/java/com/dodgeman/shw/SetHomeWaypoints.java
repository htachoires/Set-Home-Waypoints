package com.dodgeman.shw;

import com.dodgeman.shw.client.commands.HomeCommand;
import com.dodgeman.shw.client.commands.SetHomeCommand;
import com.dodgeman.shw.client.commands.ShwCommand;
import com.dodgeman.shw.client.commands.WaypointsCommand;
import com.dodgeman.shw.client.events.ClientEvents;
import com.dodgeman.shw.networking.ModMessage;
import com.dodgeman.shw.proxy.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SetHomeWaypoints.MOD_ID, name = SetHomeWaypoints.NAME, version = SetHomeWaypoints.VERSION)
public class SetHomeWaypoints {
    public static final String MOD_ID = "shw";
    public static final String NAME = "Set Home & Waypoints";
    public static final String VERSION = "2.0";

    public SetHomeWaypoints() {
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMessage.registerMessages(MOD_ID);
    }

    @SidedProxy(clientSide = "com.dodgeman.shw.proxy.ClientProxy", serverSide = "com.dodgeman.shw.proxy.ServerProxy", modId = MOD_ID)
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new HomeCommand());
        event.registerServerCommand(new SetHomeCommand());
        event.registerServerCommand(new WaypointsCommand());
        event.registerServerCommand(new ShwCommand());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.register();
        ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
    }
}
