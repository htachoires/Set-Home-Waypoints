package fr.dodge.shw.command.view;

import fr.dodge.shw.command.command.HomeCommand;
import fr.dodge.shw.command.style.StyleCommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SetHomeCommandView extends CommandView {

	public SetHomeCommandView(EntityPlayerMP player) {
		super(player);
	}

	public void messageSetHomeCreated() {
		String[] message = new String[] { "Your home has been set. ", "Use ", '/' + HomeCommand.COMMAND,
				" to teleport" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString middle = new TextComponentString(message[1]);
		TextComponentString link = new TextComponentString(message[2]);
		TextComponentString end = new TextComponentString(message[3]);

		start.setStyle(new Style().setColor(TextFormatting.GREEN));
		link.setStyle(StyleCommand.link("Want to telport now ?", HomeCommand.COMMAND));
		middle.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(start.appendSibling(middle.appendSibling(link).appendSibling(end)));
	}
}
