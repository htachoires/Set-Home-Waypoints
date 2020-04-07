package fr.dodge.shw.command.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.CommandWaypoint;
import fr.dodge.shw.command.style.CommandStyle;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

public class CommandViewWaypoint extends CommandViewBase {

	public CommandViewWaypoint(EntityPlayerMP player) {
		super(player);
	}

	public void messageSuccessAddWaypoint(EntityPlayerMP player, String name) {
		String[] message = new String[] { "commands.shw.wp.success_waypoint", "commands.shw.use",
				String.format("/%s %s %s", CommandWaypoint.COMMAND, CommandWaypoint.use, name),
				"commands.shw.wp.to_teleport_at" };

		ITextComponent success = new TextComponentTranslation(message[0]);
		ITextComponent use = new TextComponentTranslation(message[1]);
		ITextComponent command = new TextComponentString(message[2]);
		ITextComponent toTeleportAt = new TextComponentTranslation(message[3]);

		success.setStyle((new Style().setColor(TextFormatting.GREEN)));
		use.setStyle(new Style().setColor(TextFormatting.WHITE));
		command.setStyle(CommandStyle.command("commands.shw.wp.to_use_waypoint", message[2]));

		success.appendText("\n");

		player.sendMessage(success.appendSibling(use.appendSibling(command).appendSibling(toTeleportAt)));
	}

	public void messageErrorRemove(EntityPlayerMP player, String name) {
		String[] message = new String[] { "commands.shw.wp.cannot_remove", "<", name, ">. ",
				"commands.shw.wp.not_set_yet" };

		ITextComponent cannotRemove = new TextComponentTranslation(message[0]);
		ITextComponent separatorLeft = new TextComponentString(message[1]);
		ITextComponent invalidName = new TextComponentString(message[2]);
		ITextComponent separatorRight = new TextComponentString(message[3]);
		ITextComponent notSetYet = new TextComponentTranslation(message[4]);

		invalidName.setStyle(CommandStyle.command("commands.shw.wp.ask_set",
				String.format("/%s %s %s", CommandWaypoint.COMMAND, CommandWaypoint.set, message[2])));

		cannotRemove.setStyle((new Style().setColor(TextFormatting.RED)));
		separatorLeft.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(cannotRemove.appendSibling(
				separatorLeft.appendSibling(invalidName).appendSibling(separatorRight).appendSibling(notSetYet)));
	}

	public void messageSuccessRemove(EntityPlayerMP player, String name) {
		String[] message = new String[] { "commands.shw.wp.success_remove" };

		ITextComponent success = new TextComponentTranslation(message[0], name);
		success.setStyle(new Style().setColor(TextFormatting.GREEN));

		player.sendMessage(success);
	}

	public void messageErrorInvalidName(EntityPlayerMP player, String name) {
		String[] message = new String[] { "commands.shw.wp.cannot_use", name, "commands.shw.wp.as_waypoint_name",
				"commands.wp.hover_for_details" };

		ITextComponent cannotUse = new TextComponentTranslation(message[0]);
		ITextComponent invalidName = new TextComponentString(message[1]);
		ITextComponent end = new TextComponentTranslation(message[2]);
		ITextComponent hoverTextC = new TextComponentTranslation(message[3], name);

		StringJoiner fNames = new StringJoiner(", ");
		for (String n : CommandWaypoint.commandArgs) {
			fNames.add(n);
		}

		hoverTextC.setStyle(new Style().setColor(TextFormatting.GRAY));

		invalidName.setStyle(new Style().setColor(TextFormatting.RED).setHoverEvent(new HoverEvent(
				HoverEvent.Action.SHOW_TEXT,
				(new TextComponentString(CommandWaypoint.pattern + "\n")
						.setStyle(new Style().setColor(TextFormatting.GREEN)))
								.appendSibling((new TextComponentString(fNames.toString())
										.setStyle(new Style().setStrikethrough(true).setColor(TextFormatting.RED)))))));
		end.appendText("\n");
		player.sendMessage(cannotUse.appendSibling(invalidName).appendSibling(end).appendSibling(hoverTextC));
	}

	public void messageErrorUsing(EntityPlayerMP player, String command) {
		String[] message = new String[] { "commands.shw.usage",
				String.format("/%s %s ", CommandWaypoint.COMMAND, command), "commands.shw.name_var" };

		ITextComponent usage = new TextComponentTranslation(message[0]);
		ITextComponent commandTextC = new TextComponentString(message[1]);
		ITextComponent variable = new TextComponentTranslation(message[2]);

		usage.setStyle(new Style().setColor(TextFormatting.GREEN));
		commandTextC.setStyle(CommandStyle.command("commands.shw.correct_command", message[1]));
		variable.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(usage.appendSibling(commandTextC).appendSibling(variable));
	}

	public void messageListWaypoint(EntityPlayerMP player, List<String> list) {
		String[] message = new String[] { String.format("/%s %s [ ", CommandWaypoint.COMMAND, CommandWaypoint.use),
				"]" };

		ArrayList<ITextComponent> waypointsTextC = new ArrayList<>();
		String separator = ", ";
		String space = " ";
		for (String waypoint : list) {
			ITextComponent command = new TextComponentString(waypoint);

			command.setStyle(CommandStyle.command("commands.shw.wp.To_teleport_at",
					String.format("/%s %s %s", CommandWaypoint.COMMAND, CommandWaypoint.use, waypoint)));

			ITextComponent comma = new TextComponentString(
					(waypointsTextC.size() + 2 < list.size() * 2 ? separator : space));
			comma.setStyle(new Style().setColor(TextFormatting.WHITE));

			waypointsTextC.add(command);
			waypointsTextC.add(comma);
		}
		ITextComponent startTextC = new TextComponentString(message[0]);

		for (ITextComponent waypointTextC : waypointsTextC) {
			startTextC.appendSibling(waypointTextC);
		}

		ITextComponent endListTextC = new TextComponentString(message[1]);

		player.sendMessage(startTextC.appendSibling(endListTextC));
	}

	public void messageHelp() {
		String pattern = "/" + CommandWaypoint.COMMAND + " %s";
		String patternWithArg = "/" + CommandWaypoint.COMMAND + " %s ";
		ITextComponent nameVar = new TextComponentTranslation("commands.shw.name_var");

		String limitText = "====";
		ITextComponent startLimitTextC = new TextComponentString(
				String.format("%s %s %s\n", limitText, Reference.NAME, limitText));
		ITextComponent endLimitTextC = new TextComponentString(
				String.format("\n%s %s %s", limitText, Reference.NAME, limitText));
		startLimitTextC.setStyle(new Style().setColor(TextFormatting.GREEN));

		ITextComponent set = new TextComponentString(String.format(patternWithArg, CommandWaypoint.set))
				.appendSibling(nameVar).appendText("\n").setStyle(new Style().setColor(TextFormatting.WHITE));

		ITextComponent use = new TextComponentString(String.format(patternWithArg, CommandWaypoint.use))
				.appendSibling(nameVar).appendText("\n");

		ITextComponent remove = new TextComponentString(String.format(patternWithArg, CommandWaypoint.remove))
				.appendSibling(nameVar).appendText("\n");

		ITextComponent list = new TextComponentString(String.format(pattern + "\n", CommandWaypoint.list));
		ITextComponent limit = new TextComponentString(String.format(pattern + "\n", CommandWaypoint.limit));
		ITextComponent help = new TextComponentString(String.format(pattern, CommandWaypoint.help));

		sendMessage(startLimitTextC.appendSibling(set.appendSibling(use).appendSibling(remove).appendSibling(list)
				.appendSibling(limit).appendSibling(help)).appendSibling(endLimitTextC));
	}

	public void messageMaxWaypoints() {
		String[] message = new String[] { "commands.shw.wp.max" };
		ITextComponent errorMax = new TextComponentTranslation(message[0],
				SHWConfiguration.WAYPOINTS_CONFIG.MAX_WAYPOINTS).setStyle(new Style().setColor(TextFormatting.RED));
		sendMessage(errorMax);
	}

	public void messageLimitOfWaypointsPerPlayer() {
		ITextComponent limit = new TextComponentTranslation("commands.shw.wp.limit",
				SHWConfiguration.WAYPOINTS_CONFIG.MAX_WAYPOINTS);
		sendMessage(limit);
	}

	public void messageSuccessRemoveAllWaypoints() {
		ITextComponent success = new TextComponentTranslation("commands.shw.wp.success_clear");
		sendMessage(success);
	}

	public ITextComponent messageTeleportingTo(String name) {
		String[] message = new String[] { "commands.shw.wp.teleport_to", name };

		ITextComponent result = new TextComponentTranslation(message[0]);
		ITextComponent destination = new TextComponentString(message[1]);

		destination.setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY));

		return result.appendSibling(destination);
	}

}
