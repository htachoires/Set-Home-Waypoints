package fr.dodge.shw.command.command;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.view.HomeCommandView;
import fr.dodge.shw.config.SHWConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class HomeCommand extends CommandBase {

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

		EntityPlayer player = (EntityPlayer) sender;
		NBTTagCompound tag = player.getEntityData();
		HomeCommandView view = new HomeCommandView(player);
		
		long date = tag.getLong(SetHomeCommand.prefixDate + "date");
		long cooldownRemaining = new Date().getTime() - date - SHWConfiguration.HOME_CONFIG.COOLDOWN;

		if (cooldownRemaining < 0) {
			view.messageCooldown(player, TimeUnit.MILLISECONDS.toSeconds(cooldownRemaining),
					TimeUnit.MILLISECONDS.toSeconds(SHWConfiguration.HOME_CONFIG.COOLDOWN), COMMAND);
			return;
		}

		String position = tag.getString(SetHomeCommand.prefix + COMMAND);

		if (!position.isEmpty()) {
			tag.setLong(SetHomeCommand.prefixDate + "date", new Date().getTime());
			TextComponentTranslation success = new TextComponentTranslation("commands.shw.home.teleport");
			ITextComponent result = CommandManager.teleportPlayer(server, sender, position, success);
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
