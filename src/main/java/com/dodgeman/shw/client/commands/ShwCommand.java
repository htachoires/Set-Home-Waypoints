package com.dodgeman.shw.client.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class ShwCommand {

    private static final String COMMAND_NAME = "shw";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(commandSourceStack -> commandSourceStack.getEntity() instanceof ServerPlayer)
                .executes(ShwCommand::showHelp));
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        String border = "==== Set Home & Waypoints ====";
        ChatFormatting borderColor = ChatFormatting.GREEN;
        ChatFormatting commandColor = ChatFormatting.GOLD;

        MutableComponent header = new TextComponent(border + "\n").withStyle(borderColor);

        MutableComponent commandSeparator = new TextComponent(", ").withStyle(ChatFormatting.WHITE);

        MutableComponent home = new TextComponent("/" + HomeCommand.COMMAND_NAME + " [ ")
                .append(new TextComponent(HomeCommand.COMMAND_CONFIG_NAME).withStyle(commandColor))
                .append(" ]\n");

        MutableComponent setHome = new TextComponent("/" + SetHomeCommand.COMMAND_NAME).append("\n");

        MutableComponent wpFirst = new TextComponent("/" + WaypointsCommand.COMMAND_NAME + " [ ")
                .append(new TextComponent(WaypointsCommand.COMMAND_SET_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_USE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_UPDATE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_REMOVE_NAME).withStyle(commandColor))
                .append(new TextComponent(" ] <" + WaypointsCommand.ARG_NAME_FOR_WAYPOINT_NAME + ">\n").withStyle(ChatFormatting.WHITE));

        MutableComponent wpSecond = new TextComponent("/" + WaypointsCommand.COMMAND_NAME + " [ ")
                .append(new TextComponent(WaypointsCommand.COMMAND_CONFIG_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_CLEAR_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_LIST_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(new TextComponent(WaypointsCommand.COMMAND_UNDO_NAME).withStyle(commandColor))
                .append(new TextComponent(" ]\n").withStyle(ChatFormatting.WHITE));

        MutableComponent body = setHome.append(home).append(wpFirst).append(wpSecond).withStyle(ChatFormatting.WHITE);

        MutableComponent footer = new TextComponent(border).withStyle(borderColor);

        context.getSource().sendSuccess(header.append(body).append(footer), false);

        return Command.SINGLE_SUCCESS;
    }
}
