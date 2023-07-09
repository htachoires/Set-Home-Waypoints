package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.model.Home;
import com.dodgeman.shw.savedata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.savedata.SetHomeWaypointsSavedDataFactory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class HomeCommand {

    public static final String COMMAND_NAME = "home";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(COMMAND_NAME).executes(context -> setHome(context.getSource()))
        );
    }

    private static int setHome(CommandSourceStack context) throws CommandSyntaxException {
        ServerPlayer player = context.getPlayerOrException();

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        Home home = savedData.getHomeOfPlayer(player.getUUID());

        ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(home.position().dimension())));

        if (!SetHomeWaypoints.ShwConfig.allowHomeToTravelThoughDimension.get() &&
                !player.getLevel().dimension().equals(serverLevel.dimension())) {
            context.sendFailure(Component.translatable("shw.commands.home.error.notAllowedToTravelDimension"));

            return Command.SINGLE_SUCCESS;
        }

        //TODO add cooldown check

        player.teleportTo(serverLevel, home.position().x(), home.position().y(), home.position().z(), home.position().ry(), home.position().rx());

        context.sendSuccess(Component.translatable("shw.commands.home.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
