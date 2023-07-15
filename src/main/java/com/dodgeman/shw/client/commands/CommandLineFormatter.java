package com.dodgeman.shw.client.commands;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CommandLineFormatter {

    public static MutableComponent formatWaypoint(String waypointName) {
        return Component.literal(waypointName)
                .withStyle(ChatFormatting.LIGHT_PURPLE)
                .withStyle(ChatFormatting.ITALIC);
    }

    public static MutableComponent formatCommand(String... command) {
        return Component.literal("/" + String.join(" ", command))
                .withStyle(ChatFormatting.GRAY)
                .withStyle(ChatFormatting.ITALIC);
    }

    public static Component formatNbOfWaypoints(int numberOfWaypoints, int maximumNumberOfWaypoints) {
        return Component.literal(String.format("(%d/%d)", numberOfWaypoints, maximumNumberOfWaypoints))
                .withStyle(getInfoSizeColorByNbOfWaypoints(numberOfWaypoints, maximumNumberOfWaypoints));
    }

    private static ChatFormatting getInfoSizeColorByNbOfWaypoints(int nbOfWaypoints, int maxNbOfWaypoints) {
        if (nbOfWaypoints == maxNbOfWaypoints) return ChatFormatting.RED;

        if (nbOfWaypoints > maxNbOfWaypoints / 2) return ChatFormatting.YELLOW;

        return ChatFormatting.GREEN;
    }
}
