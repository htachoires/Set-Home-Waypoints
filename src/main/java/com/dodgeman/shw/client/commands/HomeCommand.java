package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeCommand extends CommandBase {
    public static final String COMMAND_NAME = "home";
    public static final String COMMAND_CONFIG_NAME = "config";
    public static final String COMMAND_COOLDOWN_NAME = "cooldown";
    public static final String COMMAND_DIMENSIONAL_TRAVEL_NAME = "dimensionalTravel";
    public static final int PERMISSION_REQUIRED_FOR_CONFIGURATION = 3;

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
        if (args.length >= 1) {
            handleConfiguration(sender, args);
            return;
        }

        if (!(sender instanceof EntityPlayer)) {
            throw new CommandException("shw.commands.home.error.senderIsNotPlayer", CommandLineFormatter.formatCommand(COMMAND_NAME));
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(((EntityPlayer) sender).getUniqueID());

        Home currentHome = playerHomeAndWaypoints.getHome();

        if (currentHome == null) {
            throw new CommandException("shw.commands.home.error.homeNotFound", CommandLineFormatter.formatCommand(SetHomeCommand.COMMAND_NAME));
        }

        WorldServer worldDestination = server.getWorld(currentHome.position().dimension());
        WorldServer currentWorld = server.getWorld(player.getEntityWorld().provider.getDimension());

        if (!ShwConfigWrapper.isDimensionalTravelAllowedForHome() && worldDestination != currentWorld) {
            throw new CommandException("shw.commands.home.error.dimensionalTravelNotAllowed");
        }

        long cooldownRemaining = TimeUnit.SECONDS.toMillis(ShwConfigWrapper.getHomeCooldown()) - playerHomeAndWaypoints.elapsedTimeOfLastHomeCommandExecution();

        if (cooldownRemaining > 0) {
            throw new CommandException("shw.commands.home.error.cooldown", TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) + 1);
        }

        PlayerTeleporter.teleportTo(server, player, currentHome.position());

        playerHomeAndWaypoints.homeCommandHasBeenExecuted();
        savedData.setDirty(true);

        sender.sendMessage(new TextComponentTranslation("shw.commands.home.success"));
    }

    private void handleConfiguration(ICommandSender sender, String[] args) throws WrongUsageException {
        if (!args[0].equals(COMMAND_CONFIG_NAME)) {
            throw new WrongUsageException("shw.commands.home.config.error.invalidArguments");
        }

        //show configuration
        if (args.length == 1) {
            sender.sendMessage(new TextComponentTranslation("shw.commands.home.config.success", new TextComponentString(String.valueOf(ShwConfigWrapper.getHomeCooldown())).setStyle(new Style().setColor(TextFormatting.BLUE)), CommandLineFormatter.formatPermitted(ShwConfigWrapper.isDimensionalTravelAllowedForHome())));
            return;
        }

        if (sender instanceof EntityPlayer) {
            int permissionLevel = sender.getServer().getPlayerList().getOppedPlayers().getPermissionLevel(((EntityPlayer) sender).getGameProfile());
            if (sender.getServer().isDedicatedServer() && permissionLevel < PERMISSION_REQUIRED_FOR_CONFIGURATION) {
                sender.sendMessage(new TextComponentTranslation("commands.generic.permission").setStyle(new Style().setColor(TextFormatting.RED)));
                return;
            }
        }

        if (args.length != 3) {
            throw new WrongUsageException("shw.commands.home.config.error.invalidArguments");
        }

        switch (args[1]) {
            case COMMAND_COOLDOWN_NAME:
                configureCooldown(sender, args[2]);
                break;
            case COMMAND_DIMENSIONAL_TRAVEL_NAME:
                configureDimensionalTravel(sender, args[2]);
                break;
            default:
                throw new WrongUsageException("shw.commands.home.config.error.invalidArguments");
        }
    }

    private void configureCooldown(ICommandSender sender, String cooldownValue) throws WrongUsageException {
        int cooldown;

        try {
            cooldown = Integer.parseInt(cooldownValue);
        } catch (NumberFormatException e) {
            throw new WrongUsageException("shw.commands.home.config.error.invalidNumber", cooldownValue);
        }

        if (cooldown < 0) {
            throw new WrongUsageException("shw.commands.home.config.error.invalidArguments");
        }

        sender.getServer().getPlayerList().sendMessage(new TextComponentTranslation("shw.commands.home.config.cooldown.success", CommandLineFormatter.formatCommand(COMMAND_NAME), new TextComponentString(cooldownValue)).setStyle(new Style().setColor(TextFormatting.GRAY)));

        ShwConfigWrapper.setHomeCooldown(cooldown);
        ShwConfigWrapper.sync();
    }

    private void configureDimensionalTravel(ICommandSender sender, String isDimensionalTravelEnabledValue) throws WrongUsageException {
        if (!isDimensionalTravelEnabledValue.equalsIgnoreCase("true") && !isDimensionalTravelEnabledValue.equalsIgnoreCase("false")) {
            throw new WrongUsageException("shw.commands.home.config.error.invalidBoolean");
        }

        boolean isDimensionalTravelEnabled = Boolean.parseBoolean(isDimensionalTravelEnabledValue);

        sender.getServer().getPlayerList().sendMessage(new TextComponentTranslation("shw.commands.home.config.dimensionalTravel.success", CommandLineFormatter.formatCommand(COMMAND_NAME), new TextComponentString(isDimensionalTravelEnabledValue.toLowerCase())).setStyle(new Style().setColor(TextFormatting.GRAY)));

        ShwConfigWrapper.setAllowDimensionalTravelForHome(isDimensionalTravelEnabled);
        ShwConfigWrapper.sync();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> availableCommands = new ArrayList<>();
        int initialValue = -1; //Used to see if sender is a player or the server
        int permissionLevel = initialValue;

        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            permissionLevel = server.getPlayerList().getOppedPlayers().getPermissionLevel(player.getGameProfile());
        }

        switch (args.length) {
            case 1:
                availableCommands.add(COMMAND_CONFIG_NAME);
                break;
            case 2:
                if (permissionLevel == initialValue || permissionLevel >= PERMISSION_REQUIRED_FOR_CONFIGURATION || !server.isDedicatedServer()) {
                    availableCommands.add(COMMAND_COOLDOWN_NAME);
                    availableCommands.add(COMMAND_DIMENSIONAL_TRAVEL_NAME);
                }
                break;
            case 3:
                if (permissionLevel == initialValue || permissionLevel >= PERMISSION_REQUIRED_FOR_CONFIGURATION || !server.isDedicatedServer()) {

                    if (args[1].equals(COMMAND_COOLDOWN_NAME)) {
                        availableCommands.add("10");
                        availableCommands.add("100");
                        availableCommands.add("3600");
                    }

                    if (args[1].equals(COMMAND_DIMENSIONAL_TRAVEL_NAME)) {
                        availableCommands.add("true");
                        availableCommands.add("false");
                    }
                }
                break;
            default:
                break;
        }

        return getListOfStringsMatchingLastWord(args, availableCommands);
    }
}
