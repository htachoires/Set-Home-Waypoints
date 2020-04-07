package fr.dodge.shw.command.view;

import fr.dodge.shw.command.CommandHome;
import fr.dodge.shw.command.style.CommandStyle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandViewSetHome extends CommandViewBase {

	public CommandViewSetHome(EntityPlayerMP player) {
		super(player);
	}

	public void messageSetHomeCreated() {
		String[] message = new String[] { "commands.shw.sethome.success", "commands.shw.use", '/' + CommandHome.COMMAND,
				"commands.shw.to_teleport" };

		TextComponentTranslation success = new TextComponentTranslation(message[0]);
		TextComponentTranslation use = new TextComponentTranslation(message[1]);
		TextComponentString command = new TextComponentString(message[2]);
		TextComponentTranslation toTeleport = new TextComponentTranslation(message[3]);

		success.setStyle(new Style().setColor(TextFormatting.GREEN));
		command.setStyle(CommandStyle.command("commands.shw.want_to_teleport", CommandHome.COMMAND));
		use.setStyle(new Style().setColor(TextFormatting.WHITE));

		player.sendMessage(success.appendSibling(use.appendSibling(command).appendSibling(toTeleport)));
	}
}
