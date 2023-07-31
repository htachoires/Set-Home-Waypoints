package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.models.WaypointName;
import net.minecraft.util.text.*;

public class CommandLineFormatter {

    public static final TextFormatting WAYPOINT_COLOR = TextFormatting.LIGHT_PURPLE;
    public static final TextFormatting COMMAND_COLOR = TextFormatting.GRAY;

    public static ITextComponent formatWaypoint(WaypointName waypointName) {
        return new TextComponentString(waypointName.value())
                .setStyle(new Style().setColor(WAYPOINT_COLOR));
    }

    public static ITextComponent formatWaypointItalic(WaypointName waypointName) {
        return new TextComponentString(waypointName.value())
                .setStyle(new Style().setColor(WAYPOINT_COLOR).setItalic(true));
    }

    public static ITextComponent formatCommand(String... command) {
        return new TextComponentString("/" + String.join(" ", command))
                .setStyle(new Style().setColor(COMMAND_COLOR).setItalic(true));
    }

    public static ITextComponent formatNbOfWaypoints(int numberOfWaypoints, int maximumNumberOfWaypoints) {
        return new TextComponentString(String.format("(%d/%d)", numberOfWaypoints, maximumNumberOfWaypoints))
                .setStyle(new Style().setColor(getInfoSizeColorByNbOfWaypoints(numberOfWaypoints, maximumNumberOfWaypoints)));
    }

    private static TextFormatting getInfoSizeColorByNbOfWaypoints(int nbOfWaypoints, int maxNbOfWaypoints) {
        if (nbOfWaypoints >= maxNbOfWaypoints) return TextFormatting.RED;

        if (nbOfWaypoints > maxNbOfWaypoints / 2) return TextFormatting.YELLOW;

        return TextFormatting.GREEN;
    }

    public static ITextComponent formatPermitted(boolean isPermitted) {
        return isPermitted
                ? new TextComponentTranslation("shw.commands.config.permitted").setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
                : new TextComponentTranslation("shw.commands.config.not_permitted").setStyle(new Style().setColor(TextFormatting.RED));
    }
}
