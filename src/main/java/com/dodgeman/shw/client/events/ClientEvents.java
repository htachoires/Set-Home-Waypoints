package com.dodgeman.shw.client.events;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.networking.ModMessage;
import com.dodgeman.shw.networking.packet.GoHomeC2SPacketMessage;
import com.dodgeman.shw.proxy.ClientProxy;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEvents {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyPressed(KeyInputEvent event) {
        if (ClientProxy.GO_HOME_KEY.isPressed()) {
            ModMessage.INSTANCE.sendToServer(new GoHomeC2SPacketMessage());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onConfigChanged(PostConfigChangedEvent event) {
        if (event.getModID().equals(SetHomeWaypoints.MOD_ID)) {
            ConfigManager.sync(SetHomeWaypoints.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
