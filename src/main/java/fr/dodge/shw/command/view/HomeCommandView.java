package fr.dodge.shw.command.view;

import fr.dodge.shw.command.command.HomeCommand;
import fr.dodge.shw.command.command.SetHomeCommand;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class HomeCommandView extends CommandView {

	public HomeCommandView(EntityPlayer player) {
		super(player);
	}

	public void messageSetHomeBefore(EntityPlayer player) {
		String[] message = new String[] { "Use ", '/' + SetHomeCommand.COMMAND, " to create you home point" };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString link = new TextComponentString(message[1]);
		TextComponentString end = new TextComponentString(message[2]);

		link.setStyle(StyleCommand.link("Want to set your home ?", SetHomeCommand.COMMAND));

		start.appendSibling(link);
		start.appendSibling(end);
		player.sendMessage(start);
	}

}
