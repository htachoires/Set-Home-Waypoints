package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static com.dodgeman.shw.client.commands.CommandLineFormatter.formatCommand;

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
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUUID());

        Home currentHome = playerHomeAndWaypoints.getHome();
        Home newHome = new Home(PositionMapper.fromPlayer(player));

        Component successMessage = null;

        if (newHome.position().isInTheNether() && !playerHomeAndWaypoints.hasAlreadySetHomeInTheNether()) {
            successMessage = Component.translatable("shw.commands.sethome.success.the_nether", Component.literal("The Nether").withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GREEN);
        }

        if (newHome.position().isInTheEnd() && !playerHomeAndWaypoints.hasAlreadySetHomeInTheEnd()) {
            successMessage = Component.translatable("shw.commands.sethome.success.the_end", Component.literal("The End").withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GREEN);
        }

        if (currentHome == null) {
            successMessage = Component.translatable("shw.commands.sethome.success.first_home", formatCommand(HomeCommand.COMMAND_NAME)).withStyle(ChatFormatting.GREEN);
        }

        if (successMessage == null) {
            successMessage = Component.translatable("shw.commands.sethome.success.update").withStyle(ChatFormatting.GREEN);
        }

        playerHomeAndWaypoints.setNewHome(newHome);
        savedData.setDirty();

        context.getSource().sendSuccess(successMessage, false);

        return Command.SINGLE_SUCCESS;
    }
}
