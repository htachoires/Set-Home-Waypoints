package com.dodgeman.shw.networking;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.networking.packet.GoHomeC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessage {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SetHomeWaypoints.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(GoHomeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GoHomeC2SPacket::new)
                .encoder(GoHomeC2SPacket::toBytes)
                .consumerMainThread(GoHomeC2SPacket::handle)
                .add();
    }

    public static <TMessage> void sendToServer(TMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static <TMessage> void sendToPlayer(TMessage message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
