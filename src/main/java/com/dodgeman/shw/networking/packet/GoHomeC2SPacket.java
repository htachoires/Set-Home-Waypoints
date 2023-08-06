package com.dodgeman.shw.networking.packet;

import com.dodgeman.shw.client.commands.HomeCommand;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GoHomeC2SPacket {
    public GoHomeC2SPacket() {
    }

    public GoHomeC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            MinecraftServer server = player.server;

            try {
                server.getCommands().getDispatcher().execute(HomeCommand.COMMAND_NAME, player.createCommandSourceStack());
            } catch (CommandSyntaxException e) {
                player.sendMessage(new TextComponent("An error occurred, sorry"), player.getUUID());
            }
        });
        return true;
    }
}
