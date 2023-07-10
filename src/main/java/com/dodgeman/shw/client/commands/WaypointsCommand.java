package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.*;
import com.dodgeman.shw.savedata.mapper.PositionMapper;
import com.dodgeman.shw.savedata.model.Waypoint;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaypointsCommand {

    public static final String COMMAND_NAME = "wp";
    private static final String COMMAND_HELP_NAME = "help";
    public static final String COMMAND_SET_NAME = "set";
    public static final String SET_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    private static final String COMMAND_UPDATE_NAME = "update";
    public static final String UPDATE_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static final String COMMAND_USE_NAME = "use";
    public static final String USE_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static final String COMMAND_LIST_NAME = "list";

    public static final String COMMAND_DELETE_NAME = "delete";
    public static final String DELETE_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static final int SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE = -1;
    private static final int SET_DUPLICATE_WAYPOINT_NAME_FAILURE = -2;
    private static final int UPDATE_WAYPOINT_NOT_FOUND_FAILURE = -1;
    public static final int USE_TRAVEL_THROUGH_DIMENSION_FAILURE = -1;
    private static final int USE_COOLDOWN_NOT_READY_FAILURE = -2;
    private static final int DELETE_WAYPOINT_NOT_FOUND_FAILURE = -1;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .then(Commands
                        .literal(COMMAND_HELP_NAME)
                        .executes(WaypointsCommand::showWaypointHelp)
                )
                .then(Commands
                        .literal(COMMAND_SET_NAME)
                        .then(Commands
                                .argument(SET_ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::setWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_UPDATE_NAME)
                        .then(Commands
                                .argument(UPDATE_ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::updateWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_USE_NAME)
                        .then(Commands
                                .argument(USE_ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::useWaypoint)
                        )
                )
                .then(Commands
                        .literal(COMMAND_LIST_NAME)
                        .executes(WaypointsCommand::listWaypoint)
                )
                .then(Commands
                        .literal(COMMAND_DELETE_NAME)
                        .then(Commands
                                .argument(DELETE_ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::deleteWaypoint)
                        )
                )
        );
    }

    private static int showWaypointHelp(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.help"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int setWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, SET_ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        if (savedData.playerHasWaypointNamed(player.getUUID(), waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.duplicateWaypoint"));

            return SET_DUPLICATE_WAYPOINT_NAME_FAILURE;
        }

        Integer maximumNumberOfWaypoints = SetHomeWaypoints.ShwConfig.maximumNumberOfWaypoints.get();
        boolean playerHasReachMaximumWaypoints = savedData.getPlayerNumberOfWaypoints(player.getUUID()) >= maximumNumberOfWaypoints;

        if (playerHasReachMaximumWaypoints) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.set.error.maximumNumberOfWaypoints"));

            return SET_MAXIMUM_WAYPOINTS_REACHED_FAILURE;
        }

        savedData.addWaypointForPlayer(player.getUUID(), new Waypoint(waypointName, PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.set.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int updateWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, UPDATE_ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        if (!savedData.playerHasWaypointNamed(player.getUUID(), waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.update.error.waypointNotFound"));

            return UPDATE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        savedData.addWaypointForPlayer(player.getUUID(), new Waypoint(waypointName, PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.update.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int useWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, USE_ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        Waypoint waypoint = savedData.getWaypointOfPlayer(player.getUUID(), waypointName);
        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(waypoint.position().dimension())));

        if (!SetHomeWaypoints.ShwConfig.allowWaypointsToTravelThoughDimension.get() &&
                !player.getLevel().dimension().equals(serverLevel.dimension())) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.notAllowedToTravelDimension"));

            return USE_TRAVEL_THROUGH_DIMENSION_FAILURE;
        }

        long lastUseWaypointCommand = savedData.getLastUseWaypointCommandOfPlayer(player.getUUID());

        long cooldownRemaining = new Date().getTime() - lastUseWaypointCommand - TimeUnit.SECONDS.toMillis(SetHomeWaypoints.ShwConfig.homeCooldown.get());

        if (cooldownRemaining <= 0) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.use.error.cooldown"));

            return USE_COOLDOWN_NOT_READY_FAILURE;
        }

        savedData.playerUsedWaypointCommand(player.getUUID());
        savedData.setDirty();

        player.teleportTo(serverLevel, waypoint.position().x(), waypoint.position().y(), waypoint.position().z(), waypoint.position().ry(), waypoint.position().rx());

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.use.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int listWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        List<String> waypoints = savedData.getWaypointsOfPlayer(player.getUUID()).stream().map(Waypoint::name).toList();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.list.success", String.join(", ", waypoints), waypoints.size(), SetHomeWaypoints.ShwConfig.maximumNumberOfWaypoints.get()), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int deleteWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, DELETE_ARG_NAME_FOR_WAYPOINT_NAME);
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        if (!savedData.playerHasWaypointNamed(player.getUUID(), waypointName)) {
            context.getSource().sendFailure(Component.translatable("shw.commands.waypoints.delete.error.waypointNotFound"));

            return DELETE_WAYPOINT_NOT_FOUND_FAILURE;
        }

        savedData.removeWaypointOfPlayer(player.getUUID(), waypointName);
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.delete.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
