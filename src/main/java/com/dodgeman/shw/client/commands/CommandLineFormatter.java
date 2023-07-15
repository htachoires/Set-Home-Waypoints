package com.dodgeman.shw.client.commands;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class CommandLineFormatter {

    @NotNull
    public static MutableComponent formatWaypoint(String waypointName) {
        return Component.literal(waypointName)
                .withStyle(ChatFormatting.LIGHT_PURPLE)
                .withStyle(ChatFormatting.ITALIC);
    }

    @NotNull
    public static MutableComponent formatCommand(String... command) {
        return Component.literal("/" + String.join(" ", command))
                .withStyle(ChatFormatting.GRAY)
                .withStyle(ChatFormatting.ITALIC);
    }
}
