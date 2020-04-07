package fr.dodge.shw.command.view;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringJoiner;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.command.WaypointCommand;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

public class WaypointCommandView extends CommandView {

	public WaypointCommandView(EntityPlayerMP player) {
		super(player);
	}

	public void messageSuccessAddWaypoint(EntityPlayerMP player, String name) {
		String[] message = new String[] { "commands.shw.wp.success_waypoint", "commands.shw.use",
				String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.use, name),
				"commands.shw.wp.to_teleport_at" };

		ITextComponent success = new TextComponentTranslation(message[0]);
		ITextComponent use = new TextComponentTranslation(message[1]);
		ITextComponent command = new TextComponentString(message[2]);
		ITextComponent toTeleportAt = new TextComponentTranslation(message[3]);

		success.setStyle((new Style().setColor(TextFormatting.GREEN)));
		use.setStyle(new Style().setColor(TextFormatting.WHITE));
		command.setStyle(StyleCommand.command("commands.shw.wp.to_use_waypoint", message[2]));

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
		ITextComponent notSetYet = new TextComponentString(message[4]);

		invalidName.setStyle(StyleCommand.command("commands.shw.wp.ask_set",
				String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.set, message[2])));

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
		for (String n : WaypointCommand.commandArgs) {
			fNames.add(n);
		}

		hoverTextC.setStyle(new Style().setColor(TextFormatting.GRAY));

		invalidName.setStyle(new Style().setColor(TextFormatting.RED).setHoverEvent(new HoverEvent(
				HoverEvent.Action.SHOW_TEXT,
				(new TextComponentString(WaypointCommand.pattern + "\n")
						.setStyle(new Style().setColor(TextFormatting.GREEN)))
								.appendSibling((new TextComponentString(fNames.toString())
										.setStyle(new Style().setStrikethrough(true).setColor(TextFormatting.RED)))))));
		end.appendText("\n");
		player.sendMessage(cannotUse.appendSibling(invalidName).appendSibling(end).appendSibling(hoverTextC));
	}

	public void messageErrorUsing(EntityPlayerMP player, String command) {
		String[] message = new String[] { "commands.shw.usage",
				String.format("/%s %s ", WaypointCommand.COMMAND, command), "commands.shw.name_var" };

		ITextComponent usage = new TextComponentTranslation(message[0]);
		ITextComponent commandTextC = new TextComponentString(message[1]);
		ITextComponent variable = new TextComponentTranslation(message[2]);

		usage.setStyle(new Style().setColor(TextFormatting.GREEN));
		commandTextC.setStyle(StyleCommand.command("commands.shw.correct_command", message[1]));
		variable.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(usage.appendSibling(commandTextC).appendSibling(variable));
	}

	public void messageListWaypoint(EntityPlayerMP player, Set<String> waypoints) {
		String[] message = new String[] { String.format("/%s %s [ ", WaypointCommand.COMMAND, WaypointCommand.use),
				"]" };

		ArrayList<ITextComponent> waypointsTextC = new ArrayList<>();
		String separator = ", ";
		String space = " ";
		for (String waypoint : waypoints) {
			ITextComponent command = new TextComponentString(waypoint);

			command.setStyle(StyleCommand.command("commands.shw.wp.To_teleport_at",
					String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.use, waypoint)));

			ITextComponent comma = new TextComponentString(
					(waypointsTextC.size() + 2 < waypoints.size() * 2 ? separator : space));
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
		String pattern = "/" + WaypointCommand.COMMAND + " %s";
		String patternWithArg = "/" + WaypointCommand.COMMAND + " %s ";
		ITextComponent nameVar = new TextComponentTranslation("commands.shw.name_var");

		String limit = "====";
		ITextComponent textStartLimit = new TextComponentString(
				String.format("%s %s %s\n", limit, Reference.NAME, limit));
		ITextComponent textEndLimit = new TextComponentString(
				String.format("\n%s %s %s", limit, Reference.NAME, limit));
		textStartLimit.setStyle(new Style().setColor(TextFormatting.GREEN));

		ITextComponent set = new TextComponentString(String.format(patternWithArg, WaypointCommand.set))
				.appendSibling(nameVar).appendText("\n").setStyle(new Style().setColor(TextFormatting.WHITE));

		ITextComponent use = new TextComponentString(String.format(patternWithArg, WaypointCommand.use))
				.appendSibling(nameVar).appendText("\n");

		ITextComponent remove = new TextComponentString(String.format(patternWithArg, WaypointCommand.remove))
				.appendSibling(nameVar).appendText("\n");

		ITextComponent list = new TextComponentString(String.format(pattern + "\n", WaypointCommand.list));
		ITextComponent help = new TextComponentString(String.format(pattern, WaypointCommand.help));

		sendMessage(textStartLimit
				.appendSibling(set.appendSibling(use).appendSibling(remove).appendSibling(list).appendSibling(help))
				.appendSibling(textEndLimit));
	}

	public void messageMaxWaypoints() {
		String[] message = new String[] { "commands.shw.wp.max" };
		ITextComponent errorMax = new TextComponentTranslation(message[0],
				SHWConfiguration.WAYPOINTS_CONFIG.MAX_WAYPOINTS).setStyle(new Style().setColor(TextFormatting.RED));
		sendMessage(errorMax);
	}

	public ITextComponent messageTeleportingTo(String name) {
		String[] message = new String[] { "commands.shw.wp.teleport_to", name };

		ITextComponent result = new TextComponentTranslation(message[0]);
		ITextComponent destination = new TextComponentString(message[1]);

		destination.setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY));

		return result.appendSibling(destination);
	}

}
