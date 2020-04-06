package fr.dodge.shw.network;

import fr.dodge.shw.command.command.HomeCommand;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MyMessage implements IMessage {

	public MyMessage() {
	}

	int toSend;

	public MyMessage(int toSend) {
		this.toSend = toSend;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(toSend);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		toSend = buf.readInt();
	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class MyMessageHandler implements IMessageHandler<MyMessage, IMessage> {
		// Do note that the default constructor is required, but implicitly defined in
		// this case

		@Override
		public IMessage onMessage(MyMessage message, MessageContext ctx) {
			// This is the player the packet was sent to the server from
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			// The value that was sent
			int amount = message.toSend;
			// Execute the action on the main server thread by adding it as a scheduled task
			serverPlayer.getServerWorld().addScheduledTask(() -> {
				FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(serverPlayer,
						HomeCommand.COMMAND);
			});
			// No response packet
			return null;
		}
	}
}
