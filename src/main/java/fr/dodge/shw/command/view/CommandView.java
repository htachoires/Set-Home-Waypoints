package fr.dodge.shw.command.view;

import fr.dodge.shw.command.command.HomeCommand;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CommandView {

	protected EntityPlayer player;

	public CommandView(EntityPlayer player) {
		this.player = player;
	}

	public void sendMessage(ITextComponent message) {
		player.sendMessage(message);
	}

	public void messageCooldown(EntityPlayer player, long time, int cooldown, String command) {
		String[] message = new String[] { "Wait ", Long.toString(Math.abs(time)) + "ms", " before use " };

		TextComponentString start = new TextComponentString(message[0]);
		TextComponentString textCooldown = new TextComponentString(message[1]);
		TextComponentString middle = new TextComponentString(message[2]);

		textCooldown.setStyle(StyleCommand.cooldown(cooldown));

		start.appendSibling(textCooldown);
		start.appendSibling(middle);
		start.appendSibling(viewLink(command));
		player.sendMessage(start);
	}

	protected ITextComponent viewLink(String command) {
		return (new TextComponentString((command.startsWith("/") ? "" : '/') + command))
				.setStyle(StyleCommand.link("Want to teleport now ?", command));
	}
}
