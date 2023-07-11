package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
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
import java.util.concurrent.TimeUnit;

public class HomeCommand {

    public static final String COMMAND_NAME = "home";
    public static final int TRAVEL_THROUGH_DIMENSION_FAILURE = -1;
    public static final int COOLDOWN_NOT_READY_FAILURE = -2;
    private static final int NO_HOME_FOUND_FAILURE = -3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(CommandSourceStack::isPlayer)
                .executes(HomeCommand::goHome)
        );
    }

    private static int goHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Home currentHome = playerHomeAndWaypoints.getCurrentHome();

        if (currentHome == null) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.homeNotFound"));

            return NO_HOME_FOUND_FAILURE;
        }

        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(currentHome.position().dimension())));

        if (!ShwConfigWrapper.allowHomeToTravelThoughDimension() &&
                !player.getLevel().dimension().equals(serverLevel.dimension())) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.notAllowedToTravelDimension"));

            return TRAVEL_THROUGH_DIMENSION_FAILURE;
        }

        long lastUseHomeCommand = playerHomeAndWaypoints.getHomeCommandLastUse();

        long cooldownRemaining = new Date().getTime() - lastUseHomeCommand - TimeUnit.SECONDS.toMillis(ShwConfigWrapper.homeCooldown());

        if (cooldownRemaining <= 0) {
            context.getSource().sendFailure(Component.translatable("shw.commands.home.error.cooldown"));

            return COOLDOWN_NOT_READY_FAILURE;
        }

        player.teleportTo(serverLevel, currentHome.position().x(), currentHome.position().y(), currentHome.position().z(), currentHome.position().ry(), currentHome.position().rx());

        playerHomeAndWaypoints.homeCommandHasBeenExecuted();
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.home.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
