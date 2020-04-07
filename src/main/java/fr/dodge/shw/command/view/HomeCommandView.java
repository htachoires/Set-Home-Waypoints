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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class HomeCommandView extends CommandView {

	public HomeCommandView(EntityPlayer player) {
		super(player);
	}

	public void messageSetHomeBefore(EntityPlayer player) {
		String[] message = new String[] { "commands.shw.use", '/' + SetHomeCommand.COMMAND, "commands.shw.home.create" };

		TextComponentTranslation use = new TextComponentTranslation(message[0]);
		TextComponentString command = new TextComponentString(message[1]);
		TextComponentTranslation create = new TextComponentTranslation(message[2]);

		command.setStyle(StyleCommand.command("commands.shw.home.ask_set_home", SetHomeCommand.COMMAND));

		player.sendMessage(use.appendSibling(command).appendSibling(create));
	}

}
