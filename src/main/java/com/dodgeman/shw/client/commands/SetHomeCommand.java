package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.savedata.HomePositionMapper;
import com.dodgeman.shw.savedata.HomeSaveData;
import com.dodgeman.shw.savedata.HomeSaveDataFactory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetHomeCommand {

    public static final String COMMAND_NAME = "sethome";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(COMMAND_NAME).executes(context -> setHome(context.getSource()))
        );
    }

    private static int setHome(CommandSourceStack context) throws CommandSyntaxException {
        ServerPlayer player = context.getPlayerOrException();

        HomeSaveData homeSaveData = HomeSaveDataFactory.instance();

        homeSaveData.setHomePositionForPlayer(player.getUUID(), HomePositionMapper.fromPlayer(player));
        homeSaveData.setDirty();

        context.sendSuccess(Component.translatable("shw.commands.sethome.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}