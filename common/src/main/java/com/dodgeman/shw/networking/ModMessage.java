package com.dodgeman.shw.networking;

import com.dodgeman.shw.client.commands.HomeCommand;
import com.dodgeman.shw.networking.packet.GoHomeC2SPacket;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Cross-loader networking, backed by the Architectury {@link NetworkManager}.
 * Registration runs on both sides from the common init: the client side makes the payload
 * sendable, the server side installs the handler.
 */
public class ModMessage {

    public static void register() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                GoHomeC2SPacket.TYPE,
                GoHomeC2SPacket.STREAM_CODEC,
                ModMessage::handleGoHome);
    }

    private static void handleGoHome(GoHomeC2SPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            MinecraftServer server = player.getServer();
            if (server == null) return;

            try {
                server.getCommands().getDispatcher().execute(HomeCommand.COMMAND_NAME, player.createCommandSourceStack());
            } catch (CommandSyntaxException e) {
                player.sendSystemMessage(Component.literal("An error occurred, sorry"));
            }
        });
    }

    public static void sendGoHome() {
        NetworkManager.sendToServer(new GoHomeC2SPacket());
    }
}
