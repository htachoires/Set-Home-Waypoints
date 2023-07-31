package com.dodgeman.shw.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ShwCommand extends CommandBase {

    private static final String COMMAND_NAME = "shw";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String border = "==== Set Home & Waypoints ====";
        TextFormatting borderColor = TextFormatting.GREEN;
        TextFormatting commandColor = TextFormatting.GOLD;

        ITextComponent header = new TextComponentString(border + "\n").setStyle(new Style().setColor(borderColor));

        ITextComponent commandSeparator = new TextComponentString(", ").setStyle(new Style().setColor(TextFormatting.WHITE));

        ITextComponent home = new TextComponentString("/" + HomeCommand.COMMAND_NAME + " [ ")
                .appendSibling(new TextComponentString(HomeCommand.COMMAND_CONFIG_NAME).setStyle(new Style().setColor(commandColor)))
                .appendText(" ]\n");

        ITextComponent setHome = new TextComponentString("/" + SetHomeCommand.COMMAND_NAME).appendText("\n");

        ITextComponent wpFirst = new TextComponentString("/" + WaypointsCommand.COMMAND_NAME + " [ ")
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_SET_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_USE_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_UPDATE_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_REMOVE_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(new TextComponentString(" ] <" + WaypointsCommand.ARG_NAME_FOR_WAYPOINT_NAME + ">\n").setStyle(new Style().setColor(TextFormatting.WHITE)));

        ITextComponent wpSecond = new TextComponentString("/" + WaypointsCommand.COMMAND_NAME + " [ ")
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_CONFIG_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_CLEAR_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_LIST_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(commandSeparator.createCopy())
                .appendSibling(new TextComponentString(WaypointsCommand.COMMAND_UNDO_NAME).setStyle(new Style().setColor(commandColor)))
                .appendSibling(new TextComponentString(" ]\n").setStyle(new Style().setColor(TextFormatting.WHITE)));

        ITextComponent body = setHome.appendSibling(home).appendSibling(wpFirst).appendSibling(wpSecond).setStyle(new Style().setColor(TextFormatting.WHITE));

        ITextComponent footer = new TextComponentString(border).setStyle(new Style().setColor(borderColor));

        sender.sendMessage(header.appendSibling(body).appendSibling(footer));
    }
}
