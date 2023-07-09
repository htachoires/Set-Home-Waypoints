package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.savedata.HomePosition;
import com.dodgeman.shw.savedata.HomeSaveData;
import com.dodgeman.shw.savedata.HomeSaveDataFactory;
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

        HomeSaveData instance = HomeSaveDataFactory.instance();

        HomePosition homePosition = instance.getHomePositionByUUID(player.getUUID());

        ServerLevel dimension = player.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(homePosition.dimension())));

        player.teleportTo(dimension, homePosition.x(), homePosition.y(), homePosition.z(), homePosition.ry(), homePosition.rx());

        context.sendSuccess(Component.translatable("shw.commands.home.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}