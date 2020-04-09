package fr.dodge.shw.client;

import fr.dodge.shw.Reference;
import fr.dodge.shw.network.SHWPacketHandler;
import fr.dodge.shw.network.TeleportToHomeMessage;
import fr.dodge.shw.proxy.ClientProxy;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SHWEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyPressed(KeyInputEvent event) {
        if (ClientProxy.KEY_HOME.isPressed()) {
            SHWPacketHandler.INSTANCE.sendToServer(new TeleportToHomeMessage());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onConfigChanged(PostConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }
    }

}
