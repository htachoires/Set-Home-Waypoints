package fr.dodge.shw.command.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class CommandManager {

	public static ITextComponent teleportPlayer(MinecraftServer server, ICommandSender sender, String position,
			ITextComponent message) {
		if (position.isEmpty())
			return (new TextComponentString("This position is not defined or is invalid !"))
					.setStyle(new Style().setColor(TextFormatting.RED));

		EntityPlayerMP player = (EntityPlayerMP) sender;

		String[] data = position.replace(',', '.').split(";");

		int actualDimension = sender.getEntityWorld().provider.getDimension();

		WorldServer worldDestination = server.getWorld(Integer.parseInt(data[0]));

		if (worldDestination != server.getWorld(actualDimension)) {
			server.getPlayerList().transferPlayerToDimension(player, Integer.parseInt(data[0]),
					new Teleporter(worldDestination));
		}

		player.connection.setPlayerLocation(Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]),
				Float.valueOf(data[4]), Float.valueOf(data[5]));
		return message;
	}

	public static String getPositionPlayer(EntityPlayer player) {
		return String.format("%d;%d.5;%d.2;%d.5;%d;1.5", player.dimension, (int) player.posX, (int) player.posY,
				(int) player.posZ, (int) player.rotationYaw, (int) player.cameraPitch);
	}
}
