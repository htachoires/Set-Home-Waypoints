package fr.dodge.shw.command;

import fr.dodge.shw.Reference;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;

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
    private static final String set = "set";
    private static final String list = "list";
    private static final String use = "use";
    private static final String remove = "remove";
    private static final String help = "help";
    private static final String limit = "limit";
    private static final String clear = "clear";

    private static final String[] commandArgs = {set, clear, help, limit, list, use, remove, SHWUtilsCommand.cooldown, SHWUtilsCommand.travelThroughDimension};
    private static final String pattern = "^[a-zA-Z0-9]{3,10}$";
    private static final String removeAll = "*";

    @Override
    public String getName() {
        return "wp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.shw.wp.usage1";
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
                    case limit:
                        sender.sendMessage(TextComponentCustom.textComponentSuccessServer("commands.shw.wp.limit", SHWConfiguration.WAYPOINTS.maxWaypoints));
                        break;
                    case clear:
                        remove(server, sender, removeAll);
                        break;
                    case set:
                    case use:
                    case remove:
                        sender.sendMessage(new TextComponentTranslation("commands.shw.wp.usage2", args[0]));
                        break;
                    case SHWUtilsCommand.cooldown:
                    case SHWUtilsCommand.travelThroughDimensionLC:
                        SHWUtilsCommand.info(sender, args[0], this.getName(), SHWConfiguration.WAYPOINTS);
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
                    case remove:
                        remove(server, sender, args[1]);
                        break;
                    case SHWUtilsCommand.cooldown:
                    case SHWUtilsCommand.travelThroughDimensionLC:
                    case SHWUtilsCommand.limit:
                        SHWUtilsCommand.help(sender, this.getName());
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
        name = name.toLowerCase(); //Avoid conflict
        if (Arrays.asList(commandArgs).contains(name))
            throw new CommandException("commands.shw.wp.set.argError");

        if (!name.matches(pattern))
            throw new CommandException("commands.shw.wp.set.nameError", TextComponentCustom.textComponentWaypoint(name), pattern);

        List<String> waypoints = getWaypoints(server, sender);
        boolean update = waypoints.contains(name);
        if (update || waypoints.size() < SHWConfiguration.WAYPOINTS.maxWaypoints) {
            SHWWorldSavedData.setString((EntityPlayer) sender, server, prefix + name, SHWUtilsCommand.getPositionPlayer((EntityPlayer) sender));
            sender.sendMessage(TextComponentCustom.textComponentSuccess(update ? "commands.shw.wp.update.success" : "commands.shw.wp.set.success", TextComponentCustom.textComponentWaypoint(name)));
        } else {
            throw new CommandException("commands.shw.wp.error.max", SHWConfiguration.WAYPOINTS.maxWaypoints);
        }
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     * @param name   Waypoint name enter by the player
     */
    private void use(MinecraftServer server, ICommandSender sender, String name) throws CommandException {
        name = name.toLowerCase();
        if (!getWaypoints(server, sender).contains(name))
            throw new CommandException("commands.shw.error.position");

        long date = SHWWorldSavedData.getLong((EntityPlayer) sender, server, prefixDate);
        long cooldownRemaining = new Date().getTime() - date - TimeUnit.SECONDS.toMillis(SHWConfiguration.WAYPOINTS.cooldown);
        if (cooldownRemaining >= 0) {
            SHWUtilsCommand.teleportPlayer(server, (EntityPlayerMP) sender,
                    SHWWorldSavedData.getString((EntityPlayer) sender, server, prefix + name), SHWConfiguration.WAYPOINTS.travelThroughDimension, this.getName());
            SHWWorldSavedData.setLong((EntityPlayer) sender, server, prefixDate, new Date().getTime());
            sender.sendMessage(new TextComponentTranslation("commands.shw.wp.success", TextComponentCustom.textComponentWaypoint(name)));
        } else {
            throw new CommandException("commands.shw.error.cooldown",
                    TextComponentCustom.textComponentCooldown(Math.addExact(1, Math.abs(TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining))), SHWConfiguration.WAYPOINTS.cooldown), "/wp use " + name);
        }
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     * @param name   Waypoint name enter by the player
     */
    private void remove(MinecraftServer server, ICommandSender sender, String name) throws CommandException {
        if (name.equals(removeAll)) {
            sender.sendMessage(TextComponentCustom.textComponentSuccess("commands.shw.wp.clear.success"));
            SHWWorldSavedData.removeAllWaypoints(server, (EntityPlayer) sender);
        } else if (SHWWorldSavedData.remove(server, (EntityPlayer) sender, prefix + name)) {
            sender.sendMessage(TextComponentCustom.textComponentSuccess("commands.shw.wp.remove.success", TextComponentCustom.textComponentWaypoint(name)));
        } else {
            throw new CommandException("commands.shw.wp.remove.error", name);
        }
    }

    /**
     * @param server Minecraft server...
     * @param sender Player that execute command
     */
    private void list(MinecraftServer server, ICommandSender sender) {
        List<String> waypoints = getWaypoints(server, sender);
        sender.sendMessage(new TextComponentTranslation("commands.shw.wp.list",
                TextComponentCustom.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.DARK_PURPLE, waypoints.toArray(new String[0]))));
    }

    private void help(ICommandSender sender) {
        String limitText = "====";
        String border = String.format("%s %s %s", limitText, Reference.NAME, limitText);
        ITextComponent limitTextC = new TextComponentString(border);

        limitTextC.setStyle(new Style().setColor(TextFormatting.GREEN));

        ITextComponent twoArgs = new TextComponentTranslation("commands.shw.wp.usage2",
                TextComponentCustom.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, set, use, remove))
                .setStyle(new Style().setColor(TextFormatting.WHITE));

        ITextComponent oneArgs = new TextComponentTranslation("commands.shw.wp.usage1",
                TextComponentCustom.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, list, clear, help));

        ITextComponent configArgs = new TextComponentTranslation("commands.shw.wp.usage1",
                TextComponentCustom.stringsToTextComponent(", ", "[ ", " ]", TextFormatting.GOLD, limit, SHWUtilsCommand.cooldown, SHWUtilsCommand.travelThroughDimension));

        sender.sendMessage(
                limitTextC.appendText("\n")
                        .appendSibling(twoArgs.appendText("\n")
                                .appendSibling(oneArgs).appendText("\n")
                                .appendSibling(configArgs).appendText("\n")
                        )
                        .appendText(border));
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
        return
                (args.length == 1) ? getListOfStringsMatchingLastWord(args, commandArgs) :
                        (args.length == 2 && (Arrays.asList(use, remove).contains(args[0].toLowerCase()))) ? getListOfStringsMatchingLastWord(args, getWaypoints(server, sender))
                                : Collections.emptyList();
    }

}
