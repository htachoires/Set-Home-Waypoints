package com.dodgeman.shw.networking.packet;

import com.dodgeman.shw.client.commands.HomeCommand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GoHomeC2SPacketMessage implements IMessage {

    public GoHomeC2SPacketMessage() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class GoHomeC2SPacketMessageHandler implements IMessageHandler<GoHomeC2SPacketMessage, IMessage> {
        @Override
        public IMessage onMessage(GoHomeC2SPacketMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            serverPlayer.getServerWorld().addScheduledTask(() -> FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(serverPlayer, HomeCommand.COMMAND_NAME));
            return null;
        }
    }
}
