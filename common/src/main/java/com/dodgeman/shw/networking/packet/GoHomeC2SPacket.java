package com.dodgeman.shw.networking.packet;

import com.dodgeman.shw.SetHomeWaypoints;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Empty client-to-server packet asking the server to run the /home command for the sender.
 * Replaces the removed Forge SimpleChannel message with the vanilla CustomPacketPayload API.
 */
public record GoHomeC2SPacket() implements CustomPacketPayload {

    public static final Type<GoHomeC2SPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SetHomeWaypoints.MOD_ID, "go_home"));

    // No payload data: a unit codec always decodes to the same empty instance and encodes nothing.
    public static final StreamCodec<RegistryFriendlyByteBuf, GoHomeC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new GoHomeC2SPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
