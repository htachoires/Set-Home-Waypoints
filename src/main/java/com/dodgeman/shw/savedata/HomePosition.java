package com.dodgeman.shw.savedata;

import net.minecraft.nbt.CompoundTag;

public record HomePosition(double x, double y, double z, float ry, float rx) {

    public CompoundTag toCompoundTag() {
        CompoundTag ppTag = new CompoundTag();

        ppTag.putDouble("x", x());
        ppTag.putDouble("y", y());
        ppTag.putDouble("z", z());
        ppTag.putFloat("ry", ry());
        ppTag.putFloat("rx", rx());

        return ppTag;
    }
}
