package com.dodgeman.shw.event;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.networking.ModMessage;
import com.dodgeman.shw.networking.packet.GoHomeC2SPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = SetHomeWaypoints.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.GO_HOME_KEY.consumeClick()) {
                ModMessage.sendToServer(new GoHomeC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SetHomeWaypoints.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.GO_HOME_KEY);
        }
    }
}

