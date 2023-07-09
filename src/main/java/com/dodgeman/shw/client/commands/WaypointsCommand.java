package com.dodgeman.shw.client.commands;

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

import java.util.List;

public class WaypointsCommand {

    public static final String COMMAND_NAME = "wp";
    public static final String COMMAND_SET_NAME = "set";
    public static final String SET_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static final String COMMAND_USE_NAME = "use";
    public static final String USE_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static final String COMMAND_LIST_NAME = "list";

    public static final String COMMAND_DELETE_NAME = "delete";
    public static final String DELETE_ARG_NAME_FOR_WAYPOINT_NAME = "waypoint mame";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .then(Commands
                        .literal(COMMAND_SET_NAME)
                        .then(Commands
                                .argument(SET_ARG_NAME_FOR_WAYPOINT_NAME, StringArgumentType.word())
                                .executes(WaypointsCommand::setWaypoint)
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

    private static int setWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, SET_ARG_NAME_FOR_WAYPOINT_NAME);

        ServerPlayer player = context.getSource().getPlayerOrException();

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        savedData.addWaypointForPlayer(player.getUUID(), new Waypoint(waypointName, PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.set.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int useWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, USE_ARG_NAME_FOR_WAYPOINT_NAME);

        ServerPlayer player = context.getSource().getPlayerOrException();

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        Waypoint waypoint = savedData.getWaypointOfPlayer(player.getUUID(), waypointName);

        ServerLevel dimension = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(waypoint.position().dimension())));

        player.teleportTo(dimension, waypoint.position().x(), waypoint.position().y(), waypoint.position().z(), waypoint.position().ry(), waypoint.position().rx());

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.use.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int listWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ServerPlayer player = context.getSource().getPlayerOrException();

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        List<String> waypoints = savedData.getWaypointsOfPlayer(player.getUUID()).stream().map(Waypoint::name).toList();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.list.success", String.format("[%s]", String.join(", ", waypoints))), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int deleteWaypoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String waypointName = StringArgumentType.getString(context, DELETE_ARG_NAME_FOR_WAYPOINT_NAME);

        ServerPlayer player = context.getSource().getPlayerOrException();

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        savedData.removeWaypointOfPlayer(player.getUUID(), waypointName);
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.waypoints.delete.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
