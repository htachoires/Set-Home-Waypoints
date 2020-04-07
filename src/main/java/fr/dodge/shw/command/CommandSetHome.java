package fr.dodge.shw.command;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.view.CommandViewSetHome;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSetHome extends CommandBase {

	public static final String prefix = "h-";
	public static final String prefixDate = "date-h";

	public static final String COMMAND = "sethome";

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.shw.sethome.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof EntityPlayerMP)) {
			TextComponentTranslation wrongSender = new TextComponentTranslation("commands.shw.error_sender");
			sender.sendMessage(wrongSender);
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		CommandViewSetHome view = new CommandViewSetHome(player);

		SHWWorldSavedData.setString(player, server, prefix + CommandHome.COMMAND,
				CommandManager.getPositionPlayer(player));

		view.messageSetHomeCreated();
	}

	/**
	 * Check if the given ICommandSender has permission to execute this command
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
