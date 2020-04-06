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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

public class WaypointCommandView extends CommandView {

	public WaypointCommandView(EntityPlayerMP player) {
		super(player);
	}

	public void messageSuccessAddWaypoint(EntityPlayerMP player, String name) {
		String[] message = new String[] { "Your waypoint has been added !\n", "Use ",
				String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.use, name),
				" to teleport at this waypoint" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString after = new TextComponentString(message[1]);
		TextComponentString link = new TextComponentString(message[2]);
		TextComponentString end = new TextComponentString(message[3]);

		start.setStyle((new Style().setColor(TextFormatting.GREEN)));
		after.setStyle(new Style().setColor(TextFormatting.WHITE));
		link.setStyle(StyleCommand.link("To use this waypoint", message[2]));

		player.sendMessage(start.appendSibling(after.appendSibling(link).appendSibling(end)));
	}

	public void messageErrorRemove(EntityPlayerMP player, String name) {
		String[] message = new String[] { "Cannot remove waypoint ", "<", name, ">. ", "It hasn't been set yet" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString after = new TextComponentString(message[1]);
		TextComponentString tName = new TextComponentString(message[2]);
		TextComponentString middle = new TextComponentString(message[3]);
		TextComponentString end = new TextComponentString(message[4]);

		tName.setStyle(StyleCommand.link("To add this waypoint",
				String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.add, message[2])));

		start.setStyle((new Style().setColor(TextFormatting.RED)));
		after.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(start.appendSibling(after.appendSibling(tName).appendSibling(middle).appendSibling(end)));
	}

	public void messageSuccessRemove(EntityPlayerMP player, String name) {
		String[] message = new String[] { "Waypoint '" + name + "' has been removed" };
		TextComponentString start = new TextComponentString(message[0]);
		start.setStyle(new Style().setColor(TextFormatting.GREEN));
		player.sendMessage(start);
	}

	public void messageErrorInvalidName(EntityPlayerMP player, String name) {
		String[] message = new String[] { "Cannot use ", name, " has waypoint name\n",
				"Hover '" + name + "' for more details" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString fName = new TextComponentString(message[1]);
		TextComponentString end = new TextComponentString(message[2]);
		TextComponentString hover = new TextComponentString(message[3]);

		StringJoiner fNames = new StringJoiner(", ");
		for (String n : WaypointCommand.commandArgs) {
			fNames.add(n);
		}

		hover.setStyle(new Style().setColor(TextFormatting.GRAY));

		fName.setStyle(new Style().setColor(TextFormatting.RED).setHoverEvent(new HoverEvent(
				HoverEvent.Action.SHOW_TEXT,
				(new TextComponentString(WaypointCommand.pattern + "\n")
						.setStyle(new Style().setColor(TextFormatting.GREEN)))
								.appendSibling((new TextComponentString(fNames.toString())
										.setStyle(new Style().setStrikethrough(true).setColor(TextFormatting.RED)))))));

		player.sendMessage(start.appendSibling(fName).appendSibling(end).appendSibling(hover));
	}

	public void messageErrorUsing(EntityPlayerMP player, String command) {
		String[] message = new String[] { "Usage: ", String.format("/%s %s ", WaypointCommand.COMMAND, command),
				"<name>" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString link = new TextComponentString(message[1]);
		TextComponentString end = new TextComponentString(message[2]);

		start.setStyle(new Style().setColor(TextFormatting.GREEN));
		link.setStyle(StyleCommand.link("To correct your command", message[1]));
		end.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(start.appendSibling(link).appendSibling(end));
	}

	public void messageListWaypoint(EntityPlayerMP player, Set<String> wp) {
		String[] message = new String[] { String.format("/%s %s [ ", WaypointCommand.COMMAND, WaypointCommand.use),
				"]" };

		ArrayList<ITextComponent> textWps = new ArrayList<>();
		String separator = ", ";
		String space = " ";
		for (String sWp : wp) {
			TextComponentString link = new TextComponentString(sWp);
			TextComponentString coma = new TextComponentString(
					(textWps.size() + 2 < wp.size() * 2 ? separator : space));
			link.setStyle(StyleCommand.link("To teleport at this waypoint",
					String.format("/%s %s %s", WaypointCommand.COMMAND, WaypointCommand.use, sWp)));
			coma.setStyle(new Style().setColor(TextFormatting.WHITE));
			textWps.add(link);
			textWps.add(coma);
		}
		TextComponentString start = new TextComponentString(message[0]);

		for (ITextComponent textWp : textWps) {
			start.appendSibling(textWp);
		}

		TextComponentString end = new TextComponentString(message[1]);

		player.sendMessage(start.appendSibling(end));
	}

	public void messageHelp() {
		String pattern = "/" + WaypointCommand.COMMAND + " %s";
		String patternWithArg = "/" + WaypointCommand.COMMAND + " %s <name>";

		String limit = "====";
		TextComponentString textStartLimit = new TextComponentString(
				String.format("%s %s %s\n", limit, Reference.NAME, limit));
		TextComponentString textEndLimit = new TextComponentString(
				String.format("\n%s %s %s", limit, Reference.NAME, limit));
		textStartLimit.setStyle(new Style().setColor(TextFormatting.GREEN));

		TextComponentString add = new TextComponentString(
				String.format(patternWithArg + "\n", WaypointCommand.add + '/' + WaypointCommand.set));
		add.setStyle(new Style().setColor(TextFormatting.WHITE));

		TextComponentString use = new TextComponentString(String.format(patternWithArg + "\n", WaypointCommand.use));

		TextComponentString remove = new TextComponentString(
				String.format(patternWithArg + "\n", WaypointCommand.remove));

		TextComponentString list = new TextComponentString(String.format(pattern + "\n", WaypointCommand.list));
		TextComponentString help = new TextComponentString(String.format(pattern, WaypointCommand.help));

		sendMessage(textStartLimit
				.appendSibling(add.appendSibling(use).appendSibling(remove).appendSibling(list).appendSibling(help))
				.appendSibling(textEndLimit));
	}

	public void messageMaxWaypoints() {
		String[] message = new String[] { "You reach the limit of waypoints (%s) ! Cannot add a new one" };
		TextComponentString start = new TextComponentString(
				String.format(message[0], SHWConfiguration.waypointsConfig.MAX_WAYPOINTS));
		start.setStyle(new Style().setColor(TextFormatting.RED));
		sendMessage(start);
	}

	public ITextComponent messageTeleportingTo(String name) {
		String[] message = new String[] { "Teleporting to ", name };

		ITextComponent result = new TextComponentString(message[0]);

		TextComponentString destination = new TextComponentString(message[1]);
		destination.setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY));

		return result.appendSibling(destination);
	}

}
