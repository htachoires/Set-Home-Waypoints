package com.dodgeman.shw.saveddata.mapper;

import com.dodgeman.shw.saveddata.model.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class PositionMapper implements CompoundMapper<Position> {

    private static final String DIMENSION_KEY = "dimension";
    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";
    private static final String Z_KEY = "z";
    private static final String RY_KEY = "ry";
    private static final String RX_KEY = "rx";

    public static Position fromPlayer(ServerPlayer player) {
        Vec3 position = player.position();
        Vec2 rotation = player.getRotationVector();
        ResourceKey<Level> dimension = player.getLevel().dimension();

        return new Position(dimension.location().toString(), position.x, position.y, position.z, rotation.y, rotation.x);
    }

    @Override
    public Position fromCompoundTag(CompoundTag tag) {
        String dimension = tag.getString(DIMENSION_KEY);
        double x = tag.getDouble(X_KEY);
        double y = tag.getDouble(Y_KEY);
        double z = tag.getDouble(Z_KEY);
        float ry = tag.getFloat(RY_KEY);
        float rx = tag.getFloat(RX_KEY);

        return new Position(dimension, x, y, z, ry, rx);
    }

    @Override
    public CompoundTag toCompoundTag(Position position) {
        CompoundTag tag = new CompoundTag();
        if (position == null) return tag;

        tag.putString(DIMENSION_KEY, position.dimension());
        tag.putDouble(X_KEY, position.x());
        tag.putDouble(Y_KEY, position.y());
        tag.putDouble(Z_KEY, position.z());
        tag.putFloat(RY_KEY, position.ry());
        tag.putFloat(RX_KEY, position.rx());

        return tag;
    }
}
