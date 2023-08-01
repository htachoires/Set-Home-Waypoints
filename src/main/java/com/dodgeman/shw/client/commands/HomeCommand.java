package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class HomeCommand {

    public static final String COMMAND_NAME = "home";
    public static final String COMMAND_CONFIG_NAME = "config";
    public static final int TRAVEL_THROUGH_DIMENSION_FAILURE = -1;
    public static final int COOLDOWN_NOT_READY_FAILURE = -2;
    private static final int NO_HOME_FOUND_FAILURE = -3;
    public static final String COMMAND_COOLDOWN_NAME = "cooldown";
    public static final String ARG_NAME_FOR_COOLDOWN = "cooldownValue";
    public static final String COMMAND_DIMENSIONAL_TRAVEL_NAME = "dimensionalTravel";
    public static final String ARG_NAME_FOR_DIMENSIONAL_TRAVEL = "dimensionalTravelValue";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(CommandSourceStack::isPlayer)
                .executes(HomeCommand::goHome)
                .then(Commands
                        .literal(COMMAND_CONFIG_NAME)
                        .executes(HomeCommand::showConfiguration)
                        .then(Commands
                                .literal(COMMAND_COOLDOWN_NAME)
                                .requires(stack -> stack.hasPermission(3))
                                .then(Commands
                                        .argument(ARG_NAME_FOR_COOLDOWN, IntegerArgumentType.integer(0))
                                        .executes(HomeCommand::configureCooldown)
                                )
                        )
                        .then(Commands
                                .literal(COMMAND_DIMENSIONAL_TRAVEL_NAME)
                                .requires(stack -> stack.hasPermission(3))
                                .then(Commands
                                        .argument(ARG_NAME_FOR_DIMENSIONAL_TRAVEL, BoolArgumentType.bool())
                                        .executes(HomeCommand::configureDimensionalTravel)
                                )
                        )
                )
        );
    }

    private static int goHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Home currentHome = playerHomeAndWaypoints.getHome();

        if (currentHome == null) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.homeNotFound", CommandLineFormatter.formatCommand(SetHomeCommand.COMMAND_NAME)));

            return NO_HOME_FOUND_FAILURE;
        }

        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(currentHome.position().dimension())));

        if (!ShwConfigWrapper.isDimensionalTravelAllowedForHome() &&
                !player.level().dimension().equals(serverLevel.dimension())) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.dimensionalTravelNotAllowed"));

            return TRAVEL_THROUGH_DIMENSION_FAILURE;
        }

        long cooldownRemaining = TimeUnit.SECONDS.toMillis(ShwConfigWrapper.getHomeCooldown()) - playerHomeAndWaypoints.elapsedTimeOfLastHomeCommandExecution();

        if (cooldownRemaining > 0) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.cooldown", TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) + 1));

            return COOLDOWN_NOT_READY_FAILURE;
        }

        player.teleportTo(serverLevel, currentHome.position().x(), currentHome.position().y(), currentHome.position().z(), currentHome.position().ry(), currentHome.position().rx());

        playerHomeAndWaypoints.homeCommandHasBeenExecuted();
        savedData.setDirty();

        context.getSource().sendSuccess(() -> Component.translatable("shw.commands.home.success"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int showConfiguration(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.translatable("shw.commands.home.config.success", Component.literal(String.valueOf(ShwConfigWrapper.getHomeCooldown())).withStyle(ChatFormatting.BLUE), CommandLineFormatter.formatPermitted(ShwConfigWrapper.isDimensionalTravelAllowedForHome())), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int configureCooldown(CommandContext<CommandSourceStack> context) {
        int cooldownValue = IntegerArgumentType.getInteger(context, ARG_NAME_FOR_COOLDOWN);

        ShwConfigWrapper.setHomeCooldown(cooldownValue);

        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.translatable("shw.commands.home.config.cooldown.success", CommandLineFormatter.formatCommand(COMMAND_NAME), Component.literal(String.valueOf(cooldownValue))).withStyle(ChatFormatting.GRAY));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int configureDimensionalTravel(CommandContext<CommandSourceStack> context) {
        boolean dimensionalTravel = BoolArgumentType.getBool(context, ARG_NAME_FOR_DIMENSIONAL_TRAVEL);

        ShwConfigWrapper.setAllowDimensionalTravelForHome(dimensionalTravel);

        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.translatable("shw.commands.home.config.dimensionalTravel.success", CommandLineFormatter.formatCommand(COMMAND_NAME), Component.literal(String.valueOf(dimensionalTravel))).withStyle(ChatFormatting.GRAY));
        }

        return Command.SINGLE_SUCCESS;
    }
}
