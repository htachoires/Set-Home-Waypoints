package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetHomeCommand {

    public static final String COMMAND_NAME = "sethome";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal(COMMAND_NAME)
                .requires(CommandSourceStack::isPlayer)
                .executes(SetHomeCommand::setHome)
        );
    }

    private static int setHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad();

        savedData.setHomeForPlayer(player.getUUID(), new Home(PositionMapper.fromPlayer(player)));
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable("shw.commands.sethome.success"), false);

        return Command.SINGLE_SUCCESS;
    }
}
