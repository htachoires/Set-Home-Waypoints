package fr.dodge.shw.command;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.view.CommandViewWaypoint;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import scala.actors.threadpool.Arrays;

public class CommandWaypoint extends CommandBase {

	public static final String set = "set";
	public static final String list = "list";
	public static final String use = "use";
	public static final String remove = "remove";
	public static final String help = "help";
	public static final String limit = "limit";

	public static final String prefix = "wp-";
	public static final String prefixDate = "date-wp";

	public static final String COMMAND = "wp";

	public static final String[] commandArgs = { set, help, limit, list, use, remove };
	public static final String pattern = "^[a-zA-Z]{3,10}$";
	public static final String removeAll = "*";

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.shw.wp.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			TextComponentTranslation wrongSender = new TextComponentTranslation("commands.shw.error_sender");
			sender.sendMessage(wrongSender);
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		CommandViewWaypoint view = new CommandViewWaypoint(player);

		if (args.length < 1) {
			help(view);
		} else if (args.length == 1) {
			switch (args[0]) {
			case list:
				list(server, player, view);
				break;
			case limit:
				limit(view);
				break;
			case set:
			case use:
			case remove:
				view.messageErrorUsing(player, args[0]);
				break;
			default:
				help(view);
				break;
			}
		} else if (args.length == 2) {
			switch (args[0]) {
			case set:
				set(server, player, args[1], view);
				break;
			case list:
				list(server, player, view);
				break;
			case use:
				use(server, player, args[1], view);
				break;
			case remove:
				remove(server, player, args[1], view);
				break;
			default:
				help(view);
				break;
			}
		}
	}

	/**
	 * 
	 * @param view View that send messages to the player
	 */
	private void limit(CommandViewWaypoint view) {
		view.messageLimitOfWaypointsPerPlayer();
	}

	/**
	 * 
	 * @param server Minecraft server...
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void set(MinecraftServer server, EntityPlayerMP player, String name, CommandViewWaypoint view) {
		if (Arrays.asList(commandArgs).stream().filter(e -> name.equals(e)).count() > 0) {
			view.messageErrorInvalidName(player, name);
			return;
		}

		if (!name.matches(pattern)) {
			view.messageErrorInvalidName(player, name);
			return;
		}

		List<String> waypoints = getWaypoints(server, player);
		if (!waypoints.contains(name) && waypoints.size() >= SHWConfiguration.WAYPOINTS_CONFIG.MAX_WAYPOINTS) {
			view.messageMaxWaypoints();
			return;
		}

		SHWWorldSavedData.setString(player, server, prefix + name, CommandManager.getPositionPlayer(player));

		view.messageSuccessAddWaypoint(player, name);
	}

	/**
	 * 
	 * @param view View that send messages to the player
	 */
	private void help(CommandViewWaypoint view) {
		view.messageHelp();
	}

	/**
	 * 
	 * @param server Minecraft server...
	 * @param player Player that execute command
	 * @param view   View that send messages to the player
	 */
	private void list(MinecraftServer server, EntityPlayerMP player, CommandViewWaypoint view) {
		view.messageListWaypoint(player, getWaypoints(server, player));
	}

	/**
	 * 
	 * @param server Minecraft server...
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void use(MinecraftServer server, EntityPlayerMP player, String name, CommandViewWaypoint view) {
		long date = SHWWorldSavedData.getLong(player, server, prefixDate);
		long cooldownRemaining = new Date().getTime() - date - SHWConfiguration.WAYPOINTS_CONFIG.COOLDOWN;

		if (cooldownRemaining < 0) {
			view.messageCooldown(player, TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining),
					TimeUnit.MILLISECONDS.toSeconds(SHWConfiguration.WAYPOINTS_CONFIG.COOLDOWN),
					String.format("/%s %s %s", COMMAND, use, name));
			return;
		}
		ITextComponent successMessage = view.messageTeleportingTo(name);
		ITextComponent result = CommandManager.teleportPlayer(server, player,
				SHWWorldSavedData.getString(player, server, prefix + name), successMessage);

		if (result.equals(successMessage)) {
			SHWWorldSavedData.setLong(player, server, prefixDate, new Date().getTime());
		}

		view.sendMessage(result);
	}

	/**
	 * 
	 * @param server Minecraft server...
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void remove(MinecraftServer server, EntityPlayerMP player, String name, CommandViewWaypoint view) {

		if (name.equals(removeAll)) {
			SHWWorldSavedData.removeAllWaypoints(server, player);
			view.messageSuccessRemoveAllWaypoints();
			return;
		}

		if (SHWWorldSavedData.remove(server, player, prefix + name)) {
			view.messageSuccessRemove(player, name);
		} else {
			view.messageErrorRemove(player, name);
		}
	}

	/**
	 * 
	 * @param server Minecraft server...
	 * @param player Player that execute command
	 * @return Set of waypoints of the player
	 */
	private List<String> getWaypoints(MinecraftServer server, EntityPlayerMP player) {
		return SHWWorldSavedData.getDataOfPlayer(player, server).stream().filter(e -> e.startsWith(prefix))
				.map(e -> e.substring(prefix.length())).collect(Collectors.toList());
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, commandArgs);
		if (args.length == 2 && (args[0].equals(remove) || args[0].equals(use))) {
			List<String> result = getListOfStringsMatchingLastWord(args, getWaypoints(server, (EntityPlayerMP) sender));
			if (args[0].equals(remove))
				result.add(removeAll);
			return result;
		}
		return Collections.emptyList();
	}

}
