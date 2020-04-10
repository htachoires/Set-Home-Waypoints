package fr.dodge.shw.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SHWUtilsCommand {

    public static void teleportPlayer(MinecraftServer server, EntityPlayerMP player, String position) {
        String[] data = position.replace(',', '.').split(";");

        WorldServer worldDestination = server.getWorld(Integer.parseInt(data[0]));

        if (server.getWorld(Integer.parseInt(data[0])) != server.getWorld(player.getEntityWorld().provider.getDimension())) {
            server.getPlayerList().transferPlayerToDimension(player, Integer.parseInt(data[0]), new Teleporter(worldDestination));
        }
        player.connection.setPlayerLocation(Double.parseDouble(data[1]), Double.parseDouble(data[2]),
                Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
    }

    public static String getPositionPlayer(EntityPlayer player) {
        return String.format("%d;%d.5;%d.2;%d.5;%d;1.5",
                player.dimension,
                (int) player.posX,
                (int) player.posY,
                (int) player.posZ,
                (int) player.rotationYaw
        );
    }
}
