package com.dodgeman.shw.savedata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class HomePositionMapper {

    public static HomePosition fromPlayer(ServerPlayer player) {
        Vec3 position = player.position();
        Vec2 rotation = player.getRotationVector();
        ResourceKey<Level> dimension = player.getLevel().dimension();

        return new HomePosition(dimension.location().toString(), position.x, position.y, position.z, rotation.y, rotation.x);
    }

    public static HomePosition fromCompoundTag(CompoundTag tag) {
        String dimension = tag.getString("dimension");
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        float ry = tag.getFloat("ry");
        float rx = tag.getFloat("rx");

        return new HomePosition(dimension, x, y, z, ry, rx);
    }

    public static CompoundTag toCompoundTag(HomePosition homePosition) {
        CompoundTag tag = new CompoundTag();

        tag.putString("dimension", homePosition.dimension());
        tag.putDouble("x", homePosition.x());
        tag.putDouble("y", homePosition.y());
        tag.putDouble("z", homePosition.z());
        tag.putFloat("ry", homePosition.ry());
        tag.putFloat("rx", homePosition.rx());

        return tag;
    }
}
