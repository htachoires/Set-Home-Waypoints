package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.models.WaypointName;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CommandLineFormatter {

    public static final ChatFormatting WAYPOINT_COLOR = ChatFormatting.LIGHT_PURPLE;
    public static final ChatFormatting COMMAND_COLOR = ChatFormatting.GRAY;

    public static MutableComponent formatWaypoint(WaypointName waypointName) {
        return Component.literal(waypointName.value())
                .withStyle(WAYPOINT_COLOR);
    }

    public static MutableComponent formatWaypointItalic(WaypointName waypointName) {
        return Component.literal(waypointName.value())
                .withStyle(WAYPOINT_COLOR)
                .withStyle(ChatFormatting.ITALIC);
    }

    public static MutableComponent formatCommand(String... command) {
        return Component.literal("/" + String.join(" ", command))
                .withStyle(COMMAND_COLOR)
                .withStyle(ChatFormatting.ITALIC);
    }

    public static MutableComponent formatNbOfWaypoints(int numberOfWaypoints, int maximumNumberOfWaypoints) {
        return Component.literal(String.format("(%d/%d)", numberOfWaypoints, maximumNumberOfWaypoints))
                .withStyle(getInfoSizeColorByNbOfWaypoints(numberOfWaypoints, maximumNumberOfWaypoints));
    }

    private static ChatFormatting getInfoSizeColorByNbOfWaypoints(int nbOfWaypoints, int maxNbOfWaypoints) {
        if (nbOfWaypoints == maxNbOfWaypoints) return ChatFormatting.RED;

        if (nbOfWaypoints > maxNbOfWaypoints / 2) return ChatFormatting.YELLOW;

        return ChatFormatting.GREEN;
    }

    public static MutableComponent formatPermitted(boolean isPermitted) {
        return isPermitted
                ? Component.translatable("shw.commands.config.permitted").withStyle(ChatFormatting.DARK_GREEN)
                : Component.translatable("shw.commands.config.not_permitted").withStyle(ChatFormatting.RED);
    }
}
