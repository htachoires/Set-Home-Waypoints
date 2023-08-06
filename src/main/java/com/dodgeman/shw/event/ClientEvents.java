package com.dodgeman.shw.event;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.networking.ModMessage;
import com.dodgeman.shw.networking.packet.GoHomeC2SPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = SetHomeWaypoints.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if(KeyBinding.GO_HOME_KEY.consumeClick()) {
                ModMessage.sendToServer(new GoHomeC2SPacket());
            }
        }
    }
}

