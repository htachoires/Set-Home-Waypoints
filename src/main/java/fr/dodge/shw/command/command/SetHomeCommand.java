package fr.dodge.shw.command.command;

import java.util.Date;

import fr.dodge.shw.Reference;
import fr.dodge.shw.command.style.StyleCommand;
import fr.dodge.shw.command.view.SetHomeCommandView;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class SetHomeCommand extends CommandBase {

	public static final String prefix = "h-";
	public static final String prefixDate = "date-" + prefix;

	public static final String COMMAND = "sethome";

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
		if (!(sender instanceof EntityPlayerMP)) {
			TextComponentTranslation wrongSender = new TextComponentTranslation("commands.shw.error_sender");
			sender.sendMessage(wrongSender);
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) sender;
		NBTTagCompound tag = player.getEntityData();
		SetHomeCommandView view = new SetHomeCommandView(player);

		tag.setString(prefix + "home", CommandManager.getPositionPlayer(player));
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
