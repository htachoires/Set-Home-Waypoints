package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;

import static com.dodgeman.shw.client.commands.CommandLineFormatter.formatCommand;

public class SetHomeCommand extends CommandBase {

    public static final String COMMAND_NAME = "sethome";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer)) {
            return;
        }

        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(((EntityPlayer) sender).getUniqueID());
        EntityPlayerMP player = (EntityPlayerMP) sender;

        Home currentHome = playerHomeAndWaypoints.getHome();
        Home newHome = new Home(PositionMapper.fromPlayer(player));

        ITextComponent successMessage = null;

        if (newHome.position().isInTheNether() && !playerHomeAndWaypoints.hasAlreadySetHomeInTheNether()) {
            successMessage = new TextComponentTranslation("shw.commands.sethome.success.the_nether", new TextComponentString("The Nether").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE))).setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        if (newHome.position().isInTheEnd() && !playerHomeAndWaypoints.hasAlreadySetHomeInTheEnd()) {
            successMessage = new TextComponentTranslation("shw.commands.sethome.success.the_end", new TextComponentString("The End").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE))).setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        if (currentHome == null) {
            successMessage = new TextComponentTranslation("shw.commands.sethome.success.first_home", formatCommand(HomeCommand.COMMAND_NAME)).setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        if (successMessage == null) {
            successMessage = new TextComponentTranslation("shw.commands.sethome.success.update").setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        playerHomeAndWaypoints.setNewHome(newHome);
        savedData.setDirty(true);

        sender.sendMessage(successMessage);
    }
}
