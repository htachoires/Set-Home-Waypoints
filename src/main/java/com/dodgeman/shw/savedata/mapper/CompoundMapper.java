package com.dodgeman.shw.savedata.mapper;

import net.minecraft.nbt.CompoundTag;

public interface CompoundMapper<T> {

    T fromCompoundTag(CompoundTag tag);

    CompoundTag toCompoundTag(T element);
}
