package fr.dodge.shw.command.view;

import fr.dodge.shw.command.command.HomeCommand;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandView {

	protected EntityPlayer player;

	public CommandView(EntityPlayer player) {
		this.player = player;
	}

	public void sendMessage(ITextComponent message) {
		player.sendMessage(message);
	}

	public void messageCooldown(EntityPlayer player, long time, long cooldown, String command) {
		String[] message = new String[] { "commands.shw.wait", "~" + Long.toString(Math.max(1, Math.abs(time))) + "s",
				"commands.shw.before_use" };

		TextComponentTranslation wait = new TextComponentTranslation(message[0]);
		TextComponentString cooldownText = new TextComponentString(message[1]);
		TextComponentTranslation beforeUse = new TextComponentTranslation(message[2]);

		cooldownText.setStyle(StyleCommand.cooldown(cooldown));

		player.sendMessage(
				wait.appendSibling(cooldownText).appendSibling(beforeUse).appendSibling(commandView(command)));
	}

	protected ITextComponent commandView(String command) {
		return (new TextComponentString((command.startsWith("/") ? "" : '/') + command))
				.setStyle(StyleCommand.command("commands.shw.want_to_teleport", command));
	}
}
