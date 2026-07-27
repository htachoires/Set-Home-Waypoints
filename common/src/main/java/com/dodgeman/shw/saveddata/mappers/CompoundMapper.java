package com.dodgeman.shw.saveddata.mappers;

import net.minecraft.nbt.CompoundTag;

public interface CompoundMapper<T> {

    T fromCompoundTag(CompoundTag tag);

    CompoundTag toCompoundTag(T element);
}
