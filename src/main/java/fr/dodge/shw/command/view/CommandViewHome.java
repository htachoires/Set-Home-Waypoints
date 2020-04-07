package fr.dodge.shw.command.view;

import fr.dodge.shw.command.CommandSetHome;
import fr.dodge.shw.command.style.CommandStyle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandViewHome extends CommandViewBase {

	public CommandViewHome(EntityPlayer player) {
		super(player);
	}

	public void messageSetHomeBefore(EntityPlayer player) {
		String[] message = new String[] { "commands.shw.use", '/' + CommandSetHome.COMMAND, "commands.shw.home.create" };

		TextComponentTranslation use = new TextComponentTranslation(message[0]);
		TextComponentString command = new TextComponentString(message[1]);
		TextComponentTranslation create = new TextComponentTranslation(message[2]);

		command.setStyle(CommandStyle.command("commands.shw.home.ask_set_home", CommandSetHome.COMMAND));

		player.sendMessage(use.appendSibling(command).appendSibling(create));
	}

}
