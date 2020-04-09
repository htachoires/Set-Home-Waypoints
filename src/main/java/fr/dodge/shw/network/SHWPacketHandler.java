package fr.dodge.shw.network;

import fr.dodge.shw.network.TeleportToHomeMessage.MyMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SHWPacketHandler {
    public static SimpleNetworkWrapper INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelMessage) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelMessage);
        // Server packets
        INSTANCE.registerMessage(MyMessageHandler.class, TeleportToHomeMessage.class, nextID(), Side.SERVER);
    }
}
