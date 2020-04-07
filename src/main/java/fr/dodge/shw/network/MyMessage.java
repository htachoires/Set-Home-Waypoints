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

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	public static class MyMessageHandler implements IMessageHandler<MyMessage, IMessage> {
		@Override
		public IMessage onMessage(MyMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			serverPlayer.getServerWorld().addScheduledTask(() -> {
				FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(serverPlayer,
						HomeCommand.COMMAND);
			});
			return null;
		}
	}
}
