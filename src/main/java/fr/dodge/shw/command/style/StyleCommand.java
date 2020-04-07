package fr.dodge.shw.command.style;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class StyleCommand {

	public static Style command(String messageKey, String command) {
		return new Style().setBold(true).setColor(TextFormatting.GOLD)
				.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
						(command.startsWith("/") ? "" : '/') + command))
				.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new TextComponentTranslation(messageKey).appendText("\n")
								.setStyle(new Style().setColor(TextFormatting.GREEN))
								.appendSibling((new TextComponentTranslation("commands.shw.clickhere"))
										.setStyle(new Style().setColor(TextFormatting.WHITE)))));
	}

	public static Style cooldown(long cooldown) {
		return new Style().setColor(TextFormatting.RED)
				.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						(new TextComponentTranslation("commands.shw.cooldown", cooldown))
								.setStyle(new Style().setColor(TextFormatting.GREEN))));
	}
}
