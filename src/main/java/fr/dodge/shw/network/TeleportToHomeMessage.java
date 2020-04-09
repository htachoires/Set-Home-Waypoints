package fr.dodge.shw.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TeleportToHomeMessage implements IMessage {

    public TeleportToHomeMessage() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class MyMessageHandler implements IMessageHandler<TeleportToHomeMessage, IMessage> {
        @Override
        public IMessage onMessage(TeleportToHomeMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            serverPlayer.getServerWorld().addScheduledTask(() -> FMLCommonHandler.instance()
                    .getMinecraftServerInstance().commandManager.executeCommand(serverPlayer, "home")
            );
            return null;
        }
    }
}
