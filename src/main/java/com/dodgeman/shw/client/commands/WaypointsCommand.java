package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.config.ShwConfigWrapper;
import com.dodgeman.shw.saveddata.SetHomeAndWaypointsSavedData;
import com.dodgeman.shw.saveddata.SetHomeWaypointsSavedDataFactory;
import com.dodgeman.shw.saveddata.mappers.PositionMapper;
import com.dodgeman.shw.saveddata.models.PlayerHomeAndWaypoints;
import com.dodgeman.shw.saveddata.models.Waypoint;
import com.dodgeman.shw.saveddata.models.WaypointName;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaypointsCommand extends CommandBase {
    public static final String COMMAND_NAME = "wp";
    public static final long TIME_BEFORE_SHOWING_UNDO_REMINDER = 86_400_000;// 86_400_000 == 1 day
    public static final long TIME_BEFORE_SHOWING_UNDO_INFORMATION = 86_400_000;// 86_400_000 == 1 day
    public static final int NB_REMOVE_WAYPOINT_SUCCESS_MESSAGE = 4;
    public static final String COMMAND_SET_NAME = "set";
    public static final String COMMAND_USE_NAME = "use";
    public static final String COMMAND_UPDATE_NAME = "update";
    public static final String COMMAND_LIST_NAME = "list";
    public static final String COMMAND_REMOVE_NAME = "remove";
    public static final String COMMAND_CLEAR_NAME = "clear";
    public static final String COMMAND_UNDO_NAME = "undo";
    public static final String COMMAND_CONFIG_NAME = "config";
    public static final String ARG_NAME_FOR_WAYPOINT_NAME = "waypoint name";
    public static final String COMMAND_COOLDOWN_NAME = "cooldown";
    public static final String COMMAND_DIMENSIONAL_TRAVEL_NAME = "dimensionalTravel";
    public static final String COMMAND_MAX_WAYPOINTS_NAME = "maximumWaypointsNumber";
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
        if (args.length == 0) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }

        if (sender instanceof EntityPlayer) {
            switch (args[0]) {
                case COMMAND_SET_NAME:
                    setWaypoint(server, sender, args);
                    return;
                case COMMAND_USE_NAME:
                    useWaypoint(server, sender, args);
                    return;
                case COMMAND_UPDATE_NAME:
                    updateWaypoint(server, sender, args);
                    return;
                case COMMAND_LIST_NAME:
                    listWaypoints(server, sender);
                    return;
                case COMMAND_REMOVE_NAME:
                    removeWaypoint(server, sender, args);
                    return;
                case COMMAND_CLEAR_NAME:
                    clearWaypoints(server, sender);
                    return;
                case COMMAND_UNDO_NAME:
                    undoWaypoints(server, sender);
                    return;
                default:
                    break;
            }
        }

        if (args[0].equals(COMMAND_CONFIG_NAME)) {
            configureWaypoints(sender, args);
        } else {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }
    }

    private void setWaypoint(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WaypointName waypointName = new WaypointName(args[1]);
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        if (playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            throw new CommandException("shw.commands.waypoints.set.error.duplicateWaypoint", CommandLineFormatter.formatWaypoint(waypointName));
        }

        boolean playerHasReachMaximumWaypoints = playerHomeAndWaypoints.getNbOfWaypoints() >= ShwConfigWrapper.getMaxNbOfWaypoints();

        if (playerHasReachMaximumWaypoints) {
            throw new CommandException("shw.commands.waypoints.set.error.maximumNumberOfWaypoints", ShwConfigWrapper.getMaxNbOfWaypoints());
        }

        ITextComponent successMessage = null;

        if (!playerHomeAndWaypoints.hasAlreadySetWaypoint()) {
            successMessage = new TextComponentTranslation("shw.commands.waypoints.set.success.first_waypoint", CommandLineFormatter.formatCommand(COMMAND_NAME, COMMAND_USE_NAME, waypointName.value())).setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();
        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));

        if (successMessage == null) {
            successMessage = new TextComponentTranslation("shw.commands.waypoints.set.success", CommandLineFormatter.formatWaypoint(waypointName), CommandLineFormatter.formatNbOfWaypoints(playerHomeAndWaypoints.getNbOfWaypoints(), ShwConfigWrapper.getMaxNbOfWaypoints())).setStyle(new Style().setColor(TextFormatting.GREEN));
        }

        sender.sendMessage(successMessage);

        long lastTimeUndoInformation = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && lastTimeUndoInformation >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.set.info.undo", CommandLineFormatter.formatWaypointItalic(waypointName), CommandLineFormatter.formatWaypointItalic(lastDeletedWaypoint.name())).setStyle(new Style().setColor(TextFormatting.YELLOW).setItalic(true)));
        }

        savedData.setDirty(true);
    }

    private void useWaypoint(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WaypointName waypointName = new WaypointName(args[1]);
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        Waypoint waypoint = playerHomeAndWaypoints.getWaypointByName(waypointName);

        if (waypoint == null) {
            throw new CommandException("shw.commands.waypoints.use.error.waypointNotFound", CommandLineFormatter.formatWaypoint(waypointName));
        }

        WorldServer worldDestination = server.getWorld(waypoint.position().dimension());
        WorldServer currentWorld = server.getWorld(player.getEntityWorld().provider.getDimension());

        if (!ShwConfigWrapper.isDimensionalTravelAllowedForWaypoints() && worldDestination != currentWorld) {
            throw new CommandException("shw.commands.waypoints.use.error.dimensionalTravelNotAllowed");
        }

        long cooldownRemaining = TimeUnit.SECONDS.toMillis(ShwConfigWrapper.getWaypointsCooldown()) - playerHomeAndWaypoints.elapsedTimeOfWaypointUseCommandExecution();
        if (cooldownRemaining > 0) {
            throw new CommandException("shw.commands.waypoints.use.error.cooldown", TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining) + 1, CommandLineFormatter.formatWaypoint(waypointName));
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();

        playerHomeAndWaypoints.waypointUseCommandHasBeenExecuted();

        PlayerTeleporter.teleportTo(server, player, waypoint.position());


        sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.use.success", waypointName.value()));

        long elapsedTimeOfLastUndoInfo = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && elapsedTimeOfLastUndoInfo >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.use.info.undo", CommandLineFormatter.formatWaypointItalic(waypointName), CommandLineFormatter.formatWaypointItalic(lastDeletedWaypoint.name())).setStyle(new Style().setColor(TextFormatting.YELLOW).setItalic(true)));
        }

        savedData.setDirty(true);
    }

    private void updateWaypoint(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WaypointName waypointName = new WaypointName(args[1]);
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            throw new CommandException("shw.commands.waypoints.update.error.waypointNotFound", CommandLineFormatter.formatWaypoint(waypointName));
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();
        playerHomeAndWaypoints.addWaypoint(new Waypoint(waypointName, PositionMapper.fromPlayer(player)));

        sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.update.success", CommandLineFormatter.formatWaypoint(waypointName)).setStyle(new Style().setColor(TextFormatting.GREEN)));

        long elapsedTimeOfLastUndoInfo = new Date().getTime() - playerHomeAndWaypoints.getUndoInformationHasBeenShownAt();

        if (lastDeletedWaypoint != null && elapsedTimeOfLastUndoInfo >= TIME_BEFORE_SHOWING_UNDO_INFORMATION) {
            playerHomeAndWaypoints.undoInformationHasBeenShown();
            sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.update.info.undo", CommandLineFormatter.formatWaypointItalic(waypointName), CommandLineFormatter.formatWaypointItalic(lastDeletedWaypoint.name())).setStyle(new Style().setColor(TextFormatting.YELLOW).setItalic(true)));
        }

        savedData.setDirty(true);
    }

    private void listWaypoints(MinecraftServer server, ICommandSender sender) {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());
        List<WaypointName> waypointsName = playerHomeAndWaypoints.getWaypointsName();

        ITextComponent successMessage = new TextComponentString("/" + COMMAND_NAME + " " + COMMAND_USE_NAME + " [ ");

        int nbOfWaypoints = waypointsName.size();

        for (int i = 0; i < nbOfWaypoints; i++) {
            successMessage.appendSibling(CommandLineFormatter.formatWaypoint(waypointsName.get(i)));

            if (i < (nbOfWaypoints - 1)) {
                successMessage.appendText(", ");
            }
        }

        successMessage
                .appendText(" ] ")
                .appendSibling(CommandLineFormatter.formatNbOfWaypoints(waypointsName.size(), ShwConfigWrapper.getMaxNbOfWaypoints()));

        sender.sendMessage(successMessage);
    }

    private void removeWaypoint(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WaypointName waypointName = new WaypointName(args[1]);
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        if (!playerHomeAndWaypoints.hasWaypointNamed(waypointName)) {
            throw new CommandException("shw.commands.waypoints.remove.error.waypointNotFound", CommandLineFormatter.formatWaypoint(waypointName));
        }

        long lastTimeDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypointAt();

        playerHomeAndWaypoints.removeWaypoint(waypointName);

        int successMessageIndex = playerHomeAndWaypoints.getRemoveWaypointSuccessMessageIndex();

        sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.remove.success." + successMessageIndex, CommandLineFormatter.formatWaypoint(waypointName), CommandLineFormatter.formatWaypoint(waypointName)));

        playerHomeAndWaypoints.updateRemoveWaypointSuccessMessageIndex(NB_REMOVE_WAYPOINT_SUCCESS_MESSAGE);

        long elapsedTimeOfLastUndo = new Date().getTime() - lastTimeDeletedWaypoint;
        if (elapsedTimeOfLastUndo >= TIME_BEFORE_SHOWING_UNDO_REMINDER || lastTimeDeletedWaypoint == 0) {
            sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.remove.info.undo", CommandLineFormatter.formatWaypointItalic(waypointName), CommandLineFormatter.formatCommand(COMMAND_NAME, COMMAND_UNDO_NAME)).setStyle(new Style().setColor(TextFormatting.YELLOW).setItalic(true)));
        }

        savedData.setDirty(true);
    }

    private void clearWaypoints(MinecraftServer server, ICommandSender sender) {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.clear.success"));

        playerHomeAndWaypoints.clearWaypoints();

        savedData.setDirty(true);
    }

    private void undoWaypoints(MinecraftServer server, ICommandSender sender) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
        PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());

        if (!playerHomeAndWaypoints.hasLastDeletedWaypoint()) {
            throw new CommandException("shw.commands.waypoints.undo.error.noLastWaypointDeletedFound");
        }

        Waypoint lastDeletedWaypoint = playerHomeAndWaypoints.getLastDeletedWaypoint();

        playerHomeAndWaypoints.undoLastDeletedWaypoint();

        sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.undo.success", CommandLineFormatter.formatWaypoint(lastDeletedWaypoint.name())).setStyle(new Style().setColor(TextFormatting.GREEN)));

        savedData.setDirty(true);
    }

    private void configureWaypoints(ICommandSender sender, String[] args) throws WrongUsageException {
        //show configuration
        if (args.length == 1) {
            sender.sendMessage(new TextComponentTranslation("shw.commands.waypoints.config.success", new TextComponentString(String.valueOf(ShwConfigWrapper.getWaypointsCooldown())).setStyle(new Style().setColor(TextFormatting.BLUE)), new TextComponentString(String.valueOf(ShwConfigWrapper.getMaxNbOfWaypoints())).setStyle(new Style().setColor(TextFormatting.BLUE)), CommandLineFormatter.formatPermitted(ShwConfigWrapper.isDimensionalTravelAllowedForWaypoints())));
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
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }

        switch (args[1]) {
            case COMMAND_COOLDOWN_NAME:
                configureCooldown(sender, args[2]);
                break;
            case COMMAND_DIMENSIONAL_TRAVEL_NAME:
                configureDimensionalTravel(sender, args[2]);
                break;
            case COMMAND_MAX_WAYPOINTS_NAME:
                configureMaxNfOfWaypoints(sender, args[2]);
                break;
            default:
                throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }
    }

    private void configureCooldown(ICommandSender sender, String cooldownValue) throws WrongUsageException {
        int cooldown;

        try {
            cooldown = Integer.parseInt(cooldownValue);
        } catch (NumberFormatException e) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidNumber", cooldownValue);
        }

        if (cooldown < 0) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }

        sender.getServer().getPlayerList().sendMessage(new TextComponentTranslation("shw.commands.waypoints.config.cooldown.success", CommandLineFormatter.formatCommand(COMMAND_NAME), new TextComponentString(cooldownValue)).setStyle(new Style().setColor(TextFormatting.GRAY)));

        ShwConfigWrapper.setWaypointsCooldown(cooldown);
        ShwConfigWrapper.sync();
    }

    private void configureDimensionalTravel(ICommandSender sender, String isDimensionalTravelEnabledValue) throws WrongUsageException {
        if (!isDimensionalTravelEnabledValue.equalsIgnoreCase("true") && !isDimensionalTravelEnabledValue.equalsIgnoreCase("false")) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidBoolean");
        }

        boolean isDimensionalTravelEnabled = Boolean.parseBoolean(isDimensionalTravelEnabledValue);

        sender.getServer().getPlayerList().sendMessage(new TextComponentTranslation("shw.commands.waypoints.config.dimensionalTravel.success", CommandLineFormatter.formatCommand(COMMAND_NAME), new TextComponentString(isDimensionalTravelEnabledValue.toLowerCase())).setStyle(new Style().setColor(TextFormatting.GRAY)));

        ShwConfigWrapper.setAllowDimensionalTravelForWaypoints(isDimensionalTravelEnabled);
        ShwConfigWrapper.sync();
    }

    private void configureMaxNfOfWaypoints(ICommandSender sender, String maxNbOfWaypointsValue) throws WrongUsageException {
        int maxNbOfWaypoints;

        try {
            maxNbOfWaypoints = Integer.parseInt(maxNbOfWaypointsValue);
        } catch (NumberFormatException e) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidNumber", maxNbOfWaypointsValue);
        }

        if (maxNbOfWaypoints < 0) {
            throw new WrongUsageException("shw.commands.waypoints.config.error.invalidArguments");
        }

        sender.getServer().getPlayerList().sendMessage(new TextComponentTranslation("shw.commands.waypoints.config.maximumNumberOfWaypoints.success", new TextComponentString(maxNbOfWaypointsValue)).setStyle(new Style().setColor(TextFormatting.GRAY)));

        ShwConfigWrapper.setMaxNbOfWaypoints(maxNbOfWaypoints);
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

            switch (args.length) {
                case 1:
                    availableCommands.add(COMMAND_SET_NAME);
                    availableCommands.add(COMMAND_USE_NAME);
                    availableCommands.add(COMMAND_UPDATE_NAME);
                    availableCommands.add(COMMAND_LIST_NAME);
                    availableCommands.add(COMMAND_REMOVE_NAME);
                    availableCommands.add(COMMAND_CLEAR_NAME);
                    availableCommands.add(COMMAND_UNDO_NAME);
                    break;
                case 2:
                    switch (args[0]) {
                        case COMMAND_USE_NAME:
                        case COMMAND_UPDATE_NAME:
                        case COMMAND_REMOVE_NAME:
                            SetHomeAndWaypointsSavedData savedData = new SetHomeWaypointsSavedDataFactory().createAndLoad(server);
                            PlayerHomeAndWaypoints playerHomeAndWaypoints = savedData.getPlayerHomeAndWaypoints(player.getUniqueID());
                            for (WaypointName waypointName : playerHomeAndWaypoints.getWaypointsName()) {
                                availableCommands.add(waypointName.value());
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        switch (args.length) {
            case 1:
                availableCommands.add(COMMAND_CONFIG_NAME);
                break;
            case 2:
                switch (args[0]) {
                    case COMMAND_CONFIG_NAME:
                        if (permissionLevel == initialValue || permissionLevel >= PERMISSION_REQUIRED_FOR_CONFIGURATION || !server.isDedicatedServer()) {
                            availableCommands.add(COMMAND_COOLDOWN_NAME);
                            availableCommands.add(COMMAND_DIMENSIONAL_TRAVEL_NAME);
                            availableCommands.add(COMMAND_MAX_WAYPOINTS_NAME);
                        }
                        break;
                    default:
                        break;
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

                    if (args[1].equals(COMMAND_MAX_WAYPOINTS_NAME)) {
                        availableCommands.add("1");
                        availableCommands.add("10");
                        availableCommands.add("15");
                        availableCommands.add("20");
                        availableCommands.add("25");
                        availableCommands.add("50");
                        availableCommands.add("100");
                    }
                }
                break;
            default:
                break;
        }

        return getListOfStringsMatchingLastWord(args, availableCommands);
    }
}
