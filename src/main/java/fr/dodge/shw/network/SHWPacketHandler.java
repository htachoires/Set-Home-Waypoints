package fr.dodge.shw.network;

import fr.dodge.shw.Reference;
import fr.dodge.shw.network.MyMessage.MyMessageHandler;
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
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

		// Server packets
		INSTANCE.registerMessage(MyMessageHandler.class, MyMessage.class, nextID(), Side.SERVER);

		// Client packets
		//INSTANCE.registerMessage(MyMessageHandler.class, MyMessage.class, ID(), Side.CLIENT);
	}
}
