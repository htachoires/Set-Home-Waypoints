package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Position;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PositionMapper implements CompoundMapper<Position> {

    private static final String DIMENSION_KEY = "dimension";
    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";
    private static final String Z_KEY = "z";
    private static final String R_YAW_KEY = "rYaw";
    private static final String R_PITCH_KEY = "rPitch";

    public static Position fromPlayer(EntityPlayer player) {
        return new Position(player.dimension, player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
    }

    @Override
    public Position fromCompoundTag(NBTTagCompound tag) {
        int dimension = tag.getInteger(DIMENSION_KEY);
        double x = tag.getDouble(X_KEY);
        double y = tag.getDouble(Y_KEY);
        double z = tag.getDouble(Z_KEY);
        float ry = tag.getFloat(R_YAW_KEY);
        float rx = tag.getFloat(R_PITCH_KEY);

        return new Position(dimension, x, y, z, ry, rx);
    }

    @Override
    public NBTTagCompound toCompoundTag(Position position) {
        NBTTagCompound tag = new NBTTagCompound();
        if (position == null) return tag;

        tag.setInteger(DIMENSION_KEY, position.dimension());
        tag.setDouble(X_KEY, position.x());
        tag.setDouble(Y_KEY, position.y());
        tag.setDouble(Z_KEY, position.z());
        tag.setFloat(R_YAW_KEY, position.rYaw());
        tag.setFloat(R_PITCH_KEY, position.rPitch());

        return tag;
    }
}
