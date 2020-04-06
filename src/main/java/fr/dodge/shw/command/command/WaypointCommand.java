package fr.dodge.shw.command.command;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.command.view.WaypointCommandView;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;
import scala.NotImplementedError;
import scala.actors.threadpool.Arrays;

public class WaypointCommand extends CommandBase {

	public static final String add = "add";
	public static final String set = "set";
	public static final String list = "list";
	public static final String use = "use";
	public static final String remove = "remove";
	public static final String help = "help";

	public static final String prefix = "wp-";
	public static final String prefixDate = "date-" + prefix;

	public static final String COMMAND = "wp";

	public static final String[] commandArgs = { add, set, help, list, use, remove };
	public static final String pattern = "^[a-zA-Z]{3,10}$";

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands." + Reference.MODID + "." + getName();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			System.err.println("Only player can use this command.");
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		NBTTagCompound tag = player.getEntityData();

		WaypointCommandView view = new WaypointCommandView(player);

		if (args.length < 1) {
			help(view);
		} else if (args.length == 1) {
			switch (args[0]) {
			case list:
				list(player, view);
				break;
			case add:
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
			case add:
			case set:
				add(player, args[1], view);
				break;
			case list:
				list(player, view);
				break;
			case use:
				use(server, sender, player, args[1], view);
				break;
			case remove:
				remove(player, args[1], view);
				break;
			default:
				help(view);
				break;
			}
		}
	}

	/**
	 * 
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void add(EntityPlayerMP player, String name, WaypointCommandView view) {
		for (String fname : commandArgs) {
			if (name.equals(fname)) {
				view.messageErrorInvalidName(player, name);
				return;
			}
		}

		if (!name.matches(pattern)) {
			view.messageErrorInvalidName(player, name);
			return;
		}

		Set<String> wp = getWaypoints(player);
		if (!wp.contains(name) && wp.size() >= SHWConfiguration.waypointsConfig.MAX_WAYPOINTS) {
			view.messageMaxWaypoints();
			return;
		}

		NBTTagCompound tag = player.getEntityData();
		tag.setString(prefix + name, CommandManager.getPositionPlayer(player));
		view.messageSuccessAddWaypoint(player, name);
	}

	/**
	 * 
	 * @param player Player that execute command
	 * @param view   View that send messages to the player
	 */
	private void help(WaypointCommandView view) {
		view.messageHelp();
	}

	/**
	 * 
	 * @param player Player that execute command
	 * @param view   View that send messages to the player
	 */
	private void list(EntityPlayerMP player, WaypointCommandView view) {
		NBTTagCompound tag = player.getEntityData();
		Set<String> wp = getWaypoints(player);
		StringJoiner res = new StringJoiner(", ");
		for (String s : wp) {
			res.add("§6" + s + "§f");
		}
		view.messageListWaypoint(player, wp);
	}

	/**
	 * 
	 * @param server MinecraftServer involved
	 * @param sender Sender that execute command
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void use(MinecraftServer server, ICommandSender sender, EntityPlayerMP player, String name,
			WaypointCommandView view) {
		NBTTagCompound tag = player.getEntityData();
		long date = tag.getLong(prefixDate + "date");
		long cooldownRemaining = new Date().getTime() - date - SHWConfiguration.waypointsConfig.COOLDOWN;
		if (cooldownRemaining < 0) {
			view.messageCooldown(player, cooldownRemaining, SHWConfiguration.waypointsConfig.COOLDOWN,
					String.format("/%s %s %s", COMMAND, use, name));
			return;
		}
		tag.setLong(prefixDate + "date", new Date().getTime());

		ITextComponent result = CommandManager.teleportPlayer(server, sender, tag.getString(prefix + name),
				view.messageTeleportingTo(name));
		view.sendMessage(result);
	}

	/**
	 * 
	 * @param player Player that execute command
	 * @param name   Waypoint name enter by the player
	 * @param view   View that send messages to the player
	 */
	private void remove(EntityPlayerMP player, String name, WaypointCommandView view) {
		NBTTagCompound tag = player.getEntityData();
		if (tag.getKeySet().remove(prefix + name)) {
			view.messageSuccessRemove(player, name);
		} else {
			view.messageErrorRemove(player, name);
		}
	}

	/**
	 * 
	 * @param player Player that execute command
	 * @return Set of waypoints of the player
	 */
	private Set<String> getWaypoints(EntityPlayerMP player) {
		NBTTagCompound tag = player.getEntityData();
		Set<String> wp = new HashSet<>();

		for (String s : tag.getKeySet()) {
			if (s.startsWith(prefix)) {
				wp.add(s.substring(prefix.length()));
			}
		}
		return wp;
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
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, commandArgs)
				: args.length == 2 && (args[0].equals(remove) || args[0].equals(use))
						? getListOfStringsMatchingLastWord(args, getWaypoints((EntityPlayerMP) sender))
						: Collections.emptyList();
	}

}
