package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
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

        Home currentHome = playerHomeAndWaypoints.getCurrentHome();
        Home newHome = new Home(PositionMapper.fromPlayer(player));

        String successMessage = "shw.commands.sethome.success.update";

        if (currentHome == null) {
            successMessage = "shw.commands.sethome.success.first_home";
        }

        if (newHome.position().isInTheNether() && !playerHomeAndWaypoints.hasAlreadySetAHomeInTheNether()) {
            successMessage = "shw.commands.sethome.success.the_nether";
        }

        if (newHome.position().isInTheEnd() && !playerHomeAndWaypoints.hasAlreadySetAHomeInTheEnd()) {
            successMessage = "shw.commands.sethome.success.the_end";
        }

        playerHomeAndWaypoints.setNewHome(newHome);
        savedData.setDirty();

        context.getSource().sendSuccess(Component.translatable(successMessage).withStyle(ChatFormatting.GREEN), false);

        return Command.SINGLE_SUCCESS;
    }
}
