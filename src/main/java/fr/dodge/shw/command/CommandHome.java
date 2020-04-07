package fr.dodge.shw.command;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.view.CommandViewHome;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandHome extends CommandBase {

	public static final String COMMAND = "home";

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands." + Reference.MODID + "." + getName();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayer)) {
			TextComponentTranslation wrongSender = new TextComponentTranslation("commands.shw.error_sender");
			sender.sendMessage(wrongSender);
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		CommandViewHome view = new CommandViewHome(player);

		long date = SHWWorldSavedData.getLong(player, server, CommandSetHome.prefixDate);
		long cooldownRemaining = new Date().getTime() - date - SHWConfiguration.HOME_CONFIG.COOLDOWN;

		if (cooldownRemaining < 0) {
			view.messageCooldown(player, TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining),
					TimeUnit.MILLISECONDS.toSeconds(SHWConfiguration.HOME_CONFIG.COOLDOWN), COMMAND);
			return;
		}

		String position = SHWWorldSavedData.getString(player, server, CommandSetHome.prefix + COMMAND);

		if (!position.isEmpty()) {
			SHWWorldSavedData.setLong(player, server, CommandSetHome.prefixDate, new Date().getTime());
			TextComponentTranslation success = new TextComponentTranslation("commands.shw.home.teleport");
			ITextComponent result = CommandManager.teleportPlayer(server, player, position, success);
			if (!success.equals(result))
				view.sendMessage(result);
		} else {
			view.messageSetHomeBefore(player);
		}
	}

	/**
	 * Check if the given ICommandSender has permission to execute this command
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

}
