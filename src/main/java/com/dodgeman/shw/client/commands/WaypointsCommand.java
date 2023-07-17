package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.*;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import com.dodgeman.shw.saveddata.models.WaypointName;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dodgeman.shw.client.commands.CommandLineFormatter.*;

public class WaypointsCommand {

    public static final String COMMAND_NAME = "wp";
    public static final long TIME_BEFORE_SHOWING_UNDO_REMINDER = 86_400_000;// 86_400_000 == 1 day
    public static final long TIME_BEFORE_SHOWING_UNDO_INFORMATION = 86_400_000;// 86_400_000 == 1 day
    public static final int NB_REMOVE_WAYPOINT_SUCCESS_MESSAGE = 4;
    private static final String COMMAND_HELP_NAME = "help";
    public static final String COMMAND_SET_NAME = "set";
    public static final String COMMAND_USE_NAME = "use";
    private static final String COMMAND_UPDATE_NAME = "update";
    public static final String COMMAND_LIST_NAME = "list";
    public static final String COMMAND_REMOVE_NAME = "remove";
    private static final String COMMAND_CLEAR_NAME = "clear";
    private static final String COMMAND_UNDO_NAME = "undo";
    private static final String COMMAND_CONFIG_NAME = "config";
    public static final String ARG_NAME_FOR_WAYPOINT_NAME = "waypoint name";
    public static final int SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE = -1;
    private static final int SET_DUPLICATE_WAYPOINT_NAME_FAILURE = -2;
    public static final int USE_TRAVEL_THROUGH_DIMENSION_FAILURE = -1;
    private static final int USE_COOLDOWN_NOT_READY_FAILURE = -2;
    private static final int USE_WAYPOINT_NOT_FOUND_FAILURE = -3;
    private static final int UPDATE_WAYPOINT_NOT_FOUND_FAILURE = -1;
    private static final int DELETE_WAYPOINT_NOT_FOUND_FAILURE = -1;
    private static final int UNDO_LAST_DELETED_WAYPOINT_FAILURE = -1;
    public static final String COMMAND_COOLDOWN_NAME = "cooldown";
    public static final String ARG_NAME_FOR_COOLDOWN = "cooldownValue";
    private static final String COMMAND_TRAVEL_THROUGH_DIMENSION_NAME = "travelThroughDimension";
    public static final String ARG_NAME_FOR_TRAVEL_THROUGH_DIMENSION = "travelThroughDimensionValue";
    private static final String ARG_NAME_FOR_MAX_WAYPOINTS = "max number of waypoints";
    private static final String COMMAND_MAX_WAYPOINTS_NAME = "maximumWaypointsNumber";


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(CommandSourceStack::isPlayer)
                .then(Commands
                        .literal(COMMAND_HELP_NAME)
                        .executes(WaypointsCommand::showHelp)
                )
                .then(Commands
                        .literal(COMMAND_SET_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::setWaypoint)
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
                        .literal(COMMAND_UPDATE_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::updateWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_LIST_NAME)
                        .executes(WaypointsCommand::listWaypoints)
                )
                .then(Commands
                        .literal(COMMAND_REMOVE_NAME)
                        .then(Commands
                                .argument(ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::removeWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_CLEAR_NAME)
                        .executes(WaypointsCommand::clearWaypoints)
                )
                .then(Commands
                        .literal(COMMAND_UNDO_NAME)
                        .executes(WaypointsCommand::undoDeletedWaypoint)
                )
                .then(Commands
                        .literal(COMMAND_CONFIG_NAME)
                        .executes(WaypointsCommand::showConfiguration)
                        .then(Commands
                                .literal(COMMAND_COOLDOWN_NAME)
                                .requires(stack -> stack.hasPermission(3))
                                .then(Commands
                                        .argument(ARG_NAME_FOR_COOLDOWN, IntegerArgumentType.integer(0))
                                        .executes(WaypointsCommand::configureCooldown)
                                )
                        )
                        .then(Commands
                                .literal(COMMAND_TRAVEL_THROUGH_DIMENSION_NAME)
                                .requires(stack -> stack.hasPermission(3))
                                .then(Commands
                                        .argument(ARG_NAME_FOR_TRAVEL_THROUGH_DIMENSION, BoolArgumentType.bool())
                                        .executes(WaypointsCommand::configureTravelThroughDimension)
                                )
                        )
                        .then(Commands
                                .literal(COMMAND_MAX_WAYPOINTS_NAME)
                                .requires(stack -> stack.hasPermission(3))
                                .then(Commands
                                        .argument(ARG_NAME_FOR_MAX_WAYPOINTS, IntegerArgumentType.integer(1, 10))
                                        .executes(WaypointsCommand::configureMaxNbOfWaypoints)
                                )
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

    private static int setWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        WaypointName waypointName = new WaypointName(StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME));
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.duplicateWaypoint", formatWaypoint(waypointName)));

            return SET_DUPLICATE_WAYPOINT_NAME_FAILURE;
        }

        boolean playerHasReachMaximumWaypoints = playerHomeAndWaypoints.getNbOfWaypoints() >= ShwConfigWrapper.maximumNumberOfWaypoints();

        if (playerHasReachMaximumWaypoints) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.maximumNumberOfWaypoints", ShwConfigWrapper.maximumNumberOfWaypoints()));

            return SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE;
        }

        Component successMessage = null;

        if (!playerHomeAndWaypoints.hasAlreadySetWaypoint()) {
            successMessage = Component.translatable("shw.commands.waypoints.set.success.first_waypoint", formatCommand(COMMAND_NAME, COMMAND_USE_NAME, waypointName.value())).withStyle(ChatFormatting.GREEN);
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();
        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));

        if (successMessage == null) {
            successMessage = Component.translatable("shw.commands.waypoints.set.success", formatWaypoint(waypointName), formatNbOfWaypoints(playerHomeAndWaypoints.getNbOfWaypoints(), ShwConfigWrapper.maximumNumberOfWaypoints())).withStyle(ChatFormatting.GREEN);
        }

        context.getSource().sendSuccess(successMessage, false);

        long lastTimeUndoInformation = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && lastTimeUndoInformation >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.set.info.undo", formatWaypointItalic(waypointName), formatWaypointItalic(lastDeletedWaypoint.name())).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC), false);
        }

        savedData.setDirty();

        return Command.SINGLE_SUCCESS;
    }

    private static int useWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        WaypointName waypointName = new WaypointName(StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME));
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Waypoint waypoint = playerHomeAndWaypoints.getWaypointByName(waypointName);

        if (waypoint == null) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.waypointNotFound", formatWaypoint(waypointName)));

            return USE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(waypoint.position().dimension())));

        if (!ShwConfigWrapper.allowWaypointsToTravelThoughDimension() &&
                !player.getLevel().dimension().equals(serverLevel.dimension())) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.notAllowedToTravelDimension"));

            return USE_TRAVEL_THROUGH_DIMENSION_FAILURE;
        }

        long cooldownRemaining = TimeUnit.SECONDS.toMillis(ShwConfigWrapper.waypointsCooldown()) - playerHomeAndWaypoints.elapsedTimeOfWaypointUseCommandExecution();
        if (cooldownRemaining > 0) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.cooldown", TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) + 1, formatWaypoint(waypointName)));

            return USE_COOLDOWN_NOT_READY_FAILURE;
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();

        playerHomeAndWaypoints.waypointUseCommandHasBeenExecuted();

        player.teleportTo(serverLevel, waypoint.position().x(), waypoint.position().y(), waypoint.position().z(), waypoint.position().ry(), waypoint.position().rx());

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.use.success", waypointName.value()), false);

        long elapsedTimeOfLastUndoInfo = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && elapsedTimeOfLastUndoInfo >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.use.info.undo", formatWaypointItalic(waypointName), formatWaypointItalic(lastDeletedWaypoint.name())).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC), false);
        }

        savedData.setDirty();

        return Command.SINGLE_SUCCESS;
    }

    private static int updateWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        WaypointName waypointName = new WaypointName(StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME));
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.update.error.waypointNotFound", formatWaypoint(waypointName)));

            return UPDATE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();
        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.update.success", formatWaypoint(waypointName)).withStyle(ChatFormatting.GREEN), false);

        long elapsedTimeOfLastUndoInfo = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && elapsedTimeOfLastUndoInfo >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.update.info.undo", formatWaypointItalic(waypointName), formatWaypointItalic(lastDeletedWaypoint.name())).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC), false);
        }

        savedData.setDirty();

        return Command.SINGLE_SUCCESS;
    }

    private static int listWaypoints(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        List<WaypointName> waypointsName = playerHomeAndWaypoints.getWaypointsName();

        MutableComponent successMessage = Component
                .literal("/" + COMMAND_NAME + " " + COMMAND_USE_NAME + " [ ");

        int nbOfWaypoints = waypointsName.size();

        for (int i = 0; i < nbOfWaypoints; i++) {
            successMessage.append(formatWaypoint(waypointsName.get(i)));

            if (i < (nbOfWaypoints - 1)) {
                successMessage.append(", ");
            }
        }

        successMessage
                .append(" ] ")
                .append(formatNbOfWaypoints(waypointsName.size(), ShwConfigWrapper.maximumNumberOfWaypoints()));

        context.getSource().sendSuccess(successMessage, false);

        return Command.SINGLE_SUCCESS;
    }

    private static int removeWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        WaypointName waypointName = new WaypointName(StringArgumentType.getString(context, ARG_NAME_FOR_WAYPOINT_NAME));
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.remove.error.waypointNotFound", formatWaypoint(waypointName)));

            return DELETE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        long lastTimeDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypointAt();

        playerHomeAndWaypoints.removeWaypoint(waypointName);

        int successMessageIndex = playerHomeAndWaypoints.getRemoveWaypointSuccessMessageIndex();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.remove.success." + successMessageIndex, formatWaypoint(waypointName), formatWaypoint(waypointName)), false);

        playerHomeAndWaypoints.updateRemoveWaypointSuccessMessageIndex(NB_REMOVE_WAYPOINT_SUCCESS_MESSAGE);

        long elapsedTimeOfLastUndo = new Date().getTime() - lastTimeDeletedWaypoint;
        if (elapsedTimeOfLastUndo >= TIME_BEFORE_SHOWING_UNDO_REMINDER || lastTimeDeletedWaypoint == 0) {
            context.getSource().sendSuccess(
                    Component.translatable("shw.commands.waypoints.remove.info.undo",
                            formatWaypointItalic(waypointName),
                            formatCommand(COMMAND_NAME, COMMAND_UNDO_NAME)).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC),
                    false);
        }

        savedData.setDirty();

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

    private static int undoDeletedWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        if (!playerHomeAndWaypoints.hasLastDeletedWaypoint()) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.undo.error.noLastWaypointDeletedFound"));

            return UNDO_LAST_DELETED_WAYPOINT_FAILURE;
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();

        playerHomeAndWaypoints.undoLastDeletedWaypoint();

        context.getSource().sendSuccess(
                Component.translatable("shw.commands.waypoints.undo.success",
                        formatWaypoint(lastDeletedWaypoint.name())).withStyle(ChatFormatting.GREEN),
                false);

        savedData.setDirty();

        return Command.SINGLE_SUCCESS;
    }

    private static int showConfiguration(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(
                Component.translatable(
                        "shw.commands.waypoints.config.success",
                        Component.literal(String.valueOf(ShwConfigWrapper.waypointsCooldown())).withStyle(ChatFormatting.BLUE),
                        Component.literal(String.valueOf(ShwConfigWrapper.maximumNumberOfWaypoints())).withStyle(ChatFormatting.BLUE),
                        formatPermitted(ShwConfigWrapper.allowWaypointsToTravelThoughDimension())),
                false);

        return Command.SINGLE_SUCCESS;
    }

    private static int configureCooldown(CommandContext<CommandSourceStack> context) {
        int cooldownValue = IntegerArgumentType.getInteger(context, ARG_NAME_FOR_COOLDOWN);

        ShwConfigWrapper.setWaypointsCooldown(cooldownValue);

        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.translatable("shw.commands.waypoints.config.cooldown.success", formatCommand(COMMAND_NAME, COMMAND_USE_NAME), Component.literal(String.valueOf(cooldownValue)).withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.GRAY));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int configureTravelThroughDimension(CommandContext<CommandSourceStack> context) {
        boolean travelThroughDimension = BoolArgumentType.getBool(context, ARG_NAME_FOR_TRAVEL_THROUGH_DIMENSION);

        ShwConfigWrapper.setAllowWaypointsToTravelThroughDimensionCooldown(travelThroughDimension);

        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.translatable("shw.commands.waypoints.config.travelThroughDimension.success", formatCommand(COMMAND_NAME, COMMAND_USE_NAME), Component.literal(String.valueOf(travelThroughDimension)).withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.GRAY));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int configureMaxNbOfWaypoints(CommandContext<CommandSourceStack> context) {
        int maxNbOfWaypoints = IntegerArgumentType.getInteger(context, ARG_NAME_FOR_MAX_WAYPOINTS);

        ShwConfigWrapper.setMaximumNumberOfWaypoints(maxNbOfWaypoints);

        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.translatable("shw.commands.waypoints.config.maximumNumberOfWaypoints.success", Component.literal(String.valueOf(maxNbOfWaypoints)).withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.GRAY));
        }

        return Command.SINGLE_SUCCESS;
    }
}
