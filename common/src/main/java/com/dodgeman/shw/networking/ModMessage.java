package com.dodgeman.shw.networking;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.client.commands.HomeCommand;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Cross-loader networking, backed by the Architectury {@link NetworkManager}.
 *
 * <p>MC 1.20.1 predates the vanilla {@code CustomPacketPayload}/{@code StreamCodec} networking
 * API used on newer versions, so the go-home message is a raw channel keyed by a
 * {@link ResourceLocation} carrying an empty buffer. Registration runs on both sides from the
 * common init: the server installs the handler, the client is cleared to send.
 */
public class ModMessage {

    public static final ResourceLocation GO_HOME =
            new ResourceLocation(SetHomeWaypoints.MOD_ID, "go_home");

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, GO_HOME, ModMessage::handleGoHome);
    }

    private static void handleGoHome(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
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
        NetworkManager.sendToServer(GO_HOME, new FriendlyByteBuf(Unpooled.buffer()));
    }
}
