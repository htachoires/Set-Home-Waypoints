package fr.dodge.shw.command.style;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class StyleCommand {

	public static Style link(String message, String command) {
		return new Style().setBold(true).setColor(TextFormatting.GOLD)
				.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
						(command.startsWith("/") ? "" : '/') + command))
				.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new TextComponentString(message + "\n").setStyle(new Style().setColor(TextFormatting.GREEN))
								.appendSibling((new TextComponentString("Click Here !"))
										.setStyle(new Style().setColor(TextFormatting.WHITE)))));
	}

	public static Style cooldown(int value) {
		return new Style().setColor(TextFormatting.RED).setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new TextComponentString("cooldown " + value + "ms"))
						.setStyle(new Style().setColor(TextFormatting.GREEN))));
	}
}
