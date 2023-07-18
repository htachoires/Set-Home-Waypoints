package com.dodgeman.shw.client.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ShwCommand {

    private static final String COMMAND_NAME = "shw";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .executes(ShwCommand::showHelp));
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        String border = "==== Set Home & Waypoints ====";
        ChatFormatting borderColor = ChatFormatting.GREEN;
        ChatFormatting commandColor = ChatFormatting.GOLD;

        MutableComponent header = Component.literal(border + "\n").withStyle(borderColor);

        MutableComponent commandSeparator = Component.literal(", ").withStyle(ChatFormatting.WHITE);

        MutableComponent first = Component.literal("/" + COMMAND_NAME + " [ ")
                .append(Component.literal(WaypointsCommand.COMMAND_SET_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_USE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_UPDATE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_REMOVE_NAME).withStyle(commandColor))
                .append(Component.literal(" ] <" + WaypointsCommand.ARG_NAME_FOR_WAYPOINT_NAME + ">\n").withStyle(ChatFormatting.WHITE));

        MutableComponent second = Component.literal("/" + COMMAND_NAME + " [ ")
                .append(Component.literal(WaypointsCommand.COMMAND_CONFIG_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_CLEAR_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_LIST_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(WaypointsCommand.COMMAND_UNDO_NAME).withStyle(commandColor))
                .append(Component.literal(" ]\n").withStyle(ChatFormatting.WHITE));

        MutableComponent body = first.append(second).withStyle(ChatFormatting.WHITE);

        MutableComponent footer = Component.literal(border).withStyle(borderColor);

        context.getSource().sendSuccess(header.append(body).append(footer), false);

        return Command.SINGLE_SUCCESS;
    }
}
