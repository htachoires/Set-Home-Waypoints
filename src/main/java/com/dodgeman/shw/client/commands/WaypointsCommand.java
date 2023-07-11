package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.*;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WaypointsCommand {

    public static final String COMMAND_NAME = "wp";
    private static final String COMMAND_HELP_NAME = "help";
    private static final String COMMAND_CONFIG_NAME = "config";
    public static final String COMMAND_SET_NAME = "set";
    private static final String COMMAND_UPDATE_NAME = "update";
    public static final String COMMAND_USE_NAME = "use";
    public static final String COMMAND_LIST_NAME = "list";
    private static final String COMMAND_CLEAR_NAME = "clear";
    public static final String COMMAND_REMOVE_NAME = "remove";
    private static final String COMMAND_UNDO_NAME = "undo";
    public static final String ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";
    public static final int SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE = -1;
    private static final int SET_DUPLICATE_WAYPOINT_NAME_FAILURE = -2;
    private static final int UPDATE_WAYPOINT_NOT_FOUND_FAILURE = -1;
    public static final int USE_TRAVEL_THROUGH_DIMENSION_FAILURE = -1;
    private static final int USE_COOLDOWN_NOT_READY_FAILURE = -2;
    private static final int USE_WAYPOINT_NOT_FOUND_FAILURE = -3;
    private static final int DELETE_WAYPOINT_NOT_FOUND_FAILURE = -1;
    private static final int UNDO_LAST_DELETED_WAYPOINT_FAILURE = -1;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(CommandSourceStack::isPlayer)
                .then(Commands
                        .literal(COMMAND_HELP_NAME)
                        .executes(WaypointsCommand::showHelp)
                )
                .then(Commands
                        .literal(COMMAND_CONFIG_NAME)
                        .executes(WaypointsCommand::showConfiguration)
                )
                .then(Commands
                        .literal(COMMAND_SET_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::setWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_UPDATE_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::updateWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_USE_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::useWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_UNDO_NAME)
                        .executes(WaypointsCommand::undoDeletedWaypoint)

                )
                .then(Commands
                        .literal(COMMAND_LIST_NAME)
                        .executes(WaypointsCommand::listWaypoint)
                )
                .then(Commands
                        .literal(COMMAND_CLEAR_NAME)
                        .executes(WaypointsCommand::clearWaypoints)
                ).then(Commands
                        .literal(COMMAND_REMOVE_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::removeWaypoint)
                        )
                )
        );
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        String border = "==== Set Home & Waypoints ====";
        ChatFormatting borderColor = ChatFormatting.GREEN;
        ChatFormatting commandColor = ChatFormatting.GOLD;

        MutableComponent header = Component.literal(border + "\n").withStyle(borderColor);

        MutableComponent commandSeparator = Component.literal(", ").withStyle(ChatFormatting.WHITE);

        MutableComponent first = Component.literal("/" + COMMAND_NAME + " [ ")
                .append(Component.literal(COMMAND_SET_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_USE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_UPDATE_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_REMOVE_NAME).withStyle(commandColor))
                .append(Component.literal(" ] <" + ARG_NAME_FOR_WAYPOINT_NAME + ">\n").withStyle(ChatFormatting.WHITE));

        MutableComponent second = Component.literal("/" + COMMAND_NAME + " [ ")
                .append(Component.literal(COMMAND_CONFIG_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_HELP_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_CLEAR_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_LIST_NAME).withStyle(commandColor))
                .append(commandSeparator.copy())
                .append(Component.literal(COMMAND_UNDO_NAME).withStyle(commandColor))
                .append(Component.literal(" ]\n").withStyle(ChatFormatting.WHITE));

        MutableComponent body = first.append(second).withStyle(ChatFormatting.WHITE);

        MutableComponent footer = Component.literal(border).withStyle(borderColor);

        context.getSource().sendSuccess(header.append(body).append(footer), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int showConfiguration(CommandContext<CommandSourceStack> context) {
        MutableComponent cooldown = Component.literal(String.format("Cooldown: %d seconds\n", ShwConfigWrapper.waypointsCooldown()));
        MutableComponent travelThroughDimension = Component.literal(String.format("Travel through dimension: %s\n", ShwConfigWrapper.allowWaypointsToTravelThoughDimension() ? "true" : "false"));
        MutableComponent limit = Component.literal(String.format("Maximum waypoints: %d", ShwConfigWrapper.maximumNumberOfWaypoints()));

        context.getSource().sendSuccess(cooldown.append(travelThroughDimension).append(limit), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int setWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.duplicateWaypoint"));

            return SET_DUPLICATE_WAYPOINT_NAME_FAILURE;
        }

        boolean playerHasReachMaximumWaypoints = playerHomeAndWaypoints.getNumberOfWaypoints() >= ShwConfigWrapper.maximumNumberOfWaypoints();

        if (playerHasReachMaximumWaypoints) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.maximumNumberOfWaypoints"));

            return SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE;
        }

        //TODO inform player that he have lost his undo if lastDeletedWaypoint is set

        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.set.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int updateWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.update.error.waypointNotFound"));

            return UPDATE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        //TODO inform player that he have lost his undo if lastDeletedWaypoint is set

        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.update.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int useWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Waypoint waypoint = playerHomeAndWaypoints.getWaypoint(waypointName);

        if (waypoint == null) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.waypointNotFound"));

            return USE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(waypoint.position().dimension())));

        if (!ShwConfigWrapper.allowWaypointsToTravelThoughDimension() &&
                !player.getLevel().dimension().equals(serverLevel.dimension())) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.notAllowedToTravelDimension"));

            return USE_TRAVEL_THROUGH_DIMENSION_FAILURE;
        }

        long lastUseWaypointCommand = playerHomeAndWaypoints.getWaypointCommandLastUse();

        long cooldownRemaining = new Date().getTime() - lastUseWaypointCommand - TimeUnit.SECONDS.toMillis(ShwConfigWrapper.waypointsCooldown());

        if (cooldownRemaining <= 0) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.cooldown"));

            return USE_COOLDOWN_NOT_READY_FAILURE;
        }

        //TODO inform player that he have lost his undo if lastDeletedWaypoint is set

        playerHomeAndWaypoints.useWaypointCommandHasBeenExecuted();
        savedData.setDirty();

        player.teleportTo(serverLevel, waypoint.position().x(), waypoint.position().y(), waypoint.position().z(), waypoint.position().ry(), waypoint.position().rx());

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.use.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int listWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Set<String> waypointsName = playerHomeAndWaypoints.getWaypointsName();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.list.success", String.join(", ", waypointsName), waypointsName.size(), ShwConfigWrapper.maximumNumberOfWaypoints()), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int clearWaypoints(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.clear.success"), false);

        playerHomeAndWaypoints.clearWaypoints();
        savedData.setDirty();

        return Command.SINGLE_SUCCESS;
    }

    private static int removeWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.remove.error.waypointNotFound"));

            return DELETE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        playerHomeAndWaypoints.removeWaypoint(waypointName);
        savedData.setDirty();

        //TODO inform player that he can undo delete but executing /wp set or /wp replace will delete the waypoint forever

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.remove.success." + new Random().nextInt(1, 5), waypointName, waypointName), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int undoDeletedWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasLastDeletedWaypoint()) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.undo.error.noLastWaypointDeletedFound"));

            return UNDO_LAST_DELETED_WAYPOINT_FAILURE;
        }

        playerHomeAndWaypoints.undoLastDeletedWaypoint();
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.undo.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
