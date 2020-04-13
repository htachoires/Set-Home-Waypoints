package fr.dodge.shw.command;

import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CommandWaypoint extends CommandBase {

    protected static final String prefix = "wp-";
    protected static final String prefixDate = "date-wp";
    protected static final String prefixUndoValue = "undo-wp";
    private static final String prefixUndoName = "undo-name-wp";

    private static final String set = "set";
    private static final String list = "list";
    private static final String use = "use";
    private static final String undo = "undo";
    private static final String remove = "remove";
    private static final String help = "help";
    private static final String clear = "clear";

    private static final String[] commandArgs = {set, clear, undo, help, list, use, remove, SHWUtilsCommand.cooldown, SHWUtilsCommand.limit, SHWUtilsCommand.travelThroughDimension};
    private static final String pattern = "^[a-zA-Z0-9]{3,10}$";
    private static final String removeAll = "*";

    @Override
    public String getName() {
        return "waypoints";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.shw.wp.usage1";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("wp");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof MinecraftServer) {
            SHWUtilsCommand.manageConfiguration(server, sender, args, this.getName(), SHWConfiguration.WAYPOINTS);
        } else if (sender instanceof EntityPlayer) {
            if (args.length < 1 || args.length > 2) {
                help(sender);
            } else if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case list:
                        list(server, sender);
                        break;
                    case clear:
                        remove(server, sender, removeAll);
                        break;
                    case undo:
                        undo(server, sender);
                        break;
                    case set:
                    case use:
                    case remove:
                        sender.sendMessage(new TextComponentTranslation("commands.shw.wp.usage2", args[0]));
                        break;
                    case SHWUtilsCommand.limit:
                    case SHWUtilsCommand.cooldown:
                    case SHWUtilsCommand.travelThroughDimensionLC:
                        SHWUtilsCommand.info(sender, args, this.getName(), SHWConfiguration.WAYPOINTS);
                        break;
                    default:
                        help(sender);
                        break;
                }
            } else {
                switch (args[0].toLowerCase()) {
                    case set:
                        set(server, sender, args[1]);
                        break;
                    case list:
                        list(server, sender);
                        break;
                    case use:
                        use(server, sender, args[1]);
                        break;
                    case undo:
                        undo(server, sender);
                        break;
                    case remove:
                        remove(server, sender, args[1]);
                        break;
                    case SHWUtilsCommand.cooldown:
                    case SHWUtilsCommand.travelThroughDimensionLC:
                    case SHWUtilsCommand.limit:
                        SHWUtilsCommand.info(sender, args, this.getName(), SHWConfiguration.WAYPOINTS);
                        break;
                    default:
                        help(sender);
                        break;
                }
            }
        }
    }


    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     * @param name   Waypoint name enter by the player
     */
    private void set(MinecraftServer server, ICommandSender sender, String name) throws CommandException {
        name = name.toLowerCase(); // To avoid conflict
        if (Arrays.asList(commandArgs).contains(name))
            throw new CommandException("commands.shw.wp.set.argError");

        if (!name.matches(pattern))
            throw new CommandException("commands.shw.wp.set.nameError", SHWUtilsTextComponent.textComponentWaypoint(name), pattern);

        List<String> waypoints = getWaypoints(server, sender);
        boolean update = waypoints.contains(name);
        if (update || waypoints.size() < SHWConfiguration.WAYPOINTS.maxWaypoints) {
            String undoPosition = SHWWorldSavedData.getString((EntityPlayer) sender, server, prefix + name);
            SHWWorldSavedData.setString((EntityPlayer) sender, server, prefix + name, SHWUtilsCommand.getPositionPlayer((EntityPlayer) sender));
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccess(update ? "commands.shw.wp.update.success" : "commands.shw.wp.set.success", SHWUtilsTextComponent.textComponentWaypoint(name)));
            if (update) {
                SHWWorldSavedData.setString((EntityPlayer) sender, server, prefixUndoName, name);
                SHWWorldSavedData.setString((EntityPlayer) sender, server, prefixUndoValue, undoPosition);
                sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.undo.override.info", this.getName(), undo));
            } else
                removeUndoSave(server, sender, "commands.shw.wp.undo.lost");
        } else
            throw new CommandException("commands.shw.wp.error.max", SHWConfiguration.WAYPOINTS.maxWaypoints);
    }

    private void removeUndoSave(MinecraftServer server, ICommandSender sender, String keyTranslation) {
        boolean undoValue = SHWWorldSavedData.remove((EntityPlayer) sender, server, prefixUndoValue);
        boolean undoName = SHWWorldSavedData.remove((EntityPlayer) sender, server, prefixUndoName);
        if (undoValue || undoName)
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer(keyTranslation));
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     * @param name   Waypoint name enter by the player
     */
    private void use(MinecraftServer server, ICommandSender sender, String name) throws CommandException {
        name = name.toLowerCase();// To avoid conflict
        List<String> waypoints = getWaypoints(server, sender);
        if (!waypoints.contains(name))
            throw new CommandException("commands.shw.error.position");

        String undoName = SHWWorldSavedData.getString((EntityPlayer) sender, server, prefixUndoName);

        long date = SHWWorldSavedData.getLong((EntityPlayer) sender, server, prefixDate);
        long cooldownRemaining = new Date().getTime() - date - TimeUnit.SECONDS.toMillis(SHWConfiguration.WAYPOINTS.cooldown);
        if (cooldownRemaining >= 0) {
            SHWUtilsCommand.teleportPlayer(server, (EntityPlayerMP) sender,
                    SHWWorldSavedData.getString((EntityPlayer) sender, server, prefix + name), SHWConfiguration.WAYPOINTS.travelThroughDimension, this.getName());
            SHWWorldSavedData.setLong((EntityPlayer) sender, server, prefixDate, new Date().getTime());
            sender.sendMessage(new TextComponentTranslation("commands.shw.wp.success", SHWUtilsTextComponent.textComponentWaypoint(name)));
            if (waypoints.contains(undoName))
                removeUndoSave(server, sender,"commands.shw.wp.undo.override.lost");
        } else
            throw new CommandException("commands.shw.error.cooldown",
                    SHWUtilsTextComponent.textComponentCooldown(Math.addExact(1, Math.abs(TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining))), SHWConfiguration.WAYPOINTS.cooldown), "/wp use " + name);
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     * @param name   Waypoint name enter by the player
     */
    private void remove(MinecraftServer server, ICommandSender sender, String name) throws CommandException {
        name = name.toLowerCase(); // To avoid conflict
        String undoSave = SHWWorldSavedData.getString((EntityPlayer) sender, server, prefix + name);
        if (name.equals(removeAll)) {
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccess("commands.shw.wp.clear.success"));
            SHWWorldSavedData.removeAllWaypoints(server, (EntityPlayer) sender);
        } else if (SHWWorldSavedData.remove((EntityPlayer) sender, server, prefix + name)) {
            SHWWorldSavedData.setString((EntityPlayer) sender, server, prefixUndoName, name);
            SHWWorldSavedData.setString((EntityPlayer) sender, server, prefixUndoValue, undoSave);

            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccess("commands.shw.wp.remove.success", SHWUtilsTextComponent.textComponentWaypoint(name)));
            sender.sendMessage(SHWUtilsTextComponent.textComponentSuccessServer("commands.shw.undo.remove.info", this.getName(), undo));
        } else
            throw new CommandException("commands.shw.wp.remove.error", name);
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     */
    private void undo(MinecraftServer server, ICommandSender sender) throws CommandException {
        if (getWaypoints(server, sender).size() < SHWConfiguration.WAYPOINTS.maxWaypoints) {
            String undoName = SHWWorldSavedData.getString((EntityPlayer) sender, server, prefixUndoName);
            String undoSave = SHWWorldSavedData.getString((EntityPlayer) sender, server, prefixUndoValue);
            if (!undoName.isEmpty() && !undoSave.isEmpty()) {
                SHWWorldSavedData.setString((EntityPlayer) sender, server, prefix + undoName, undoSave);
                sender.sendMessage(SHWUtilsTextComponent.textComponentSuccess("commands.shw.wp.undo.success", SHWUtilsTextComponent.textComponentWaypoint(undoName)));
                SHWWorldSavedData.remove((EntityPlayer) sender, server, prefixUndoName);
                SHWWorldSavedData.remove((EntityPlayer) sender, server, prefixUndoValue);
            } else
                throw new CommandException("commands.shw.wp.undo.error");
        } else
            throw new CommandException("commands.shw.wp.undo.error.limit", SHWConfiguration.WAYPOINTS.maxWaypoints);

    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     */
    private void list(MinecraftServer server, ICommandSender sender) {
        List<String> waypoints = getWaypoints(server, sender);
        sender.sendMessage(new TextComponentTranslation("commands.shw.wp.list",
                SHWUtilsTextComponent.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.LIGHT_PURPLE, waypoints.toArray(new String[0])))
                .appendSibling(SHWUtilsTextComponent.textComponentNumberWaypoint(waypoints.size())));
    }

    private void help(ICommandSender sender) {
        ITextComponent oneArgs = new TextComponentTranslation("commands.shw.wp.usage1",
                SHWUtilsTextComponent.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, clear, help, list, undo));

        ITextComponent twoArgs = new TextComponentTranslation("commands.shw.wp.usage2",
                SHWUtilsTextComponent.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, set, use, remove))
                .setStyle(new Style().setColor(TextFormatting.WHITE));

        ITextComponent configArgs = new TextComponentTranslation("commands.shw.wp.usage1",
                SHWUtilsTextComponent.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, SHWUtilsCommand.commandArgs));

        sender.sendMessage(
                SHWUtilsTextComponent.getBorder(true, "")
                        .appendSibling(twoArgs.appendText("\n")
                                .appendSibling(oneArgs).appendText("\n")
                                .appendSibling(configArgs).appendText("\n")
                        )
                        .appendSibling(SHWUtilsTextComponent.getBorder(false, "")));
    }

    /**
     * @param server Minecraft server...
     * @return Set of waypoints of the player
     */
    private List<String> getWaypoints(MinecraftServer server, ICommandSender sender) {
        return SHWWorldSavedData.getDataOfPlayer((EntityPlayerMP) sender, server)
                .stream()
                .filter(e -> e.startsWith(prefix))
                .map(e -> e.substring(prefix.length()))
                .collect(Collectors.toList());
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (sender instanceof EntityPlayer) {
            if (args.length == 1)
                return getListOfStringsMatchingLastWord(args, commandArgs);
            else if ((args.length == 2 && (Arrays.asList(use, remove).contains(args[0].toLowerCase()))))
                return getListOfStringsMatchingLastWord(args, getWaypoints(server, sender));
        } else if (sender instanceof MinecraftServer)
            if (args.length == 1)
                return getListOfStringsMatchingLastWord(args, SHWUtilsCommand.commandArgs);
            else if (args.length == 2 && Arrays.asList(SHWUtilsCommand.travelThroughDimensionLC).contains(args[0].toLowerCase()))
                return getListOfStringsMatchingLastWord(args, "true", "false");
            else if (args.length == 2 && Arrays.asList(SHWUtilsCommand.cooldown, SHWUtilsCommand.limit).contains(args[0].toLowerCase()))
                return getListOfStringsMatchingLastWord(args, "10");
        return Collections.emptyList();
    }

}
