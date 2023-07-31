package com.dodgeman.shw.networking;

import com.dodgeman.shw.networking.packet.GoHomeC2SPacketMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModMessage {
    public static SimpleNetworkWrapper INSTANCE;
    private static int packetId = 0;

    private static int nextPacketId() {
        return packetId++;
    }

    public static void registerMessages(String channelMessage) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelMessage);
        INSTANCE.registerMessage(GoHomeC2SPacketMessage.GoHomeC2SPacketMessageHandler.class, GoHomeC2SPacketMessage.class, nextPacketId(), Side.SERVER);
    }
}
