package com.dodgeman.shw.client.commands;

import com.dodgeman.shw.saveddata.models.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

public class PlayerTeleporter {
    public static void teleportTo(MinecraftServer server, EntityPlayerMP player, Position position) {
        WorldServer worldDestination = server.getWorld(position.dimension());
        WorldServer currentWorld = server.getWorld(player.getEntityWorld().provider.getDimension());

        if (worldDestination != currentWorld) {
            ((Entity) player).changeDimension(position.dimension(), new CommandTeleporter(position));
        } else {
            player.connection.setPlayerLocation(position.x(), position.y(), position.z(), position.rYaw(), position.rPitch());
        }
    }

    private static class CommandTeleporter implements ITeleporter {
        private final Position destination;

        private CommandTeleporter(Position destination) {
            this.destination = destination;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw) {
            BlockPos blockPosition = new BlockPos(destination.x(), destination.y(), destination.z());

            entity.moveToBlockPosAndAngles(blockPosition, destination.rYaw(), destination.rPitch());
        }
    }
}
