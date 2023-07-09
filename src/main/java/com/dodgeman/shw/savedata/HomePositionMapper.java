package com.dodgeman.shw.savedata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class HomePositionMapper {

    public static HomePosition fromPosition(Vec3 position, Vec2 rotationVector) {
        return new HomePosition(position.x, position.y, position.z, rotationVector.y, rotationVector.x);
    }

    public static HomePosition fromCompoundTag(CompoundTag tag) {
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        float ry = tag.getFloat("ry");
        float rx = tag.getFloat("rx");

        return new HomePosition(x, y, z, ry, rx);
    }
}
