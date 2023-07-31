package com.dodgeman.shw.saveddata.mappers;

import net.minecraft.nbt.NBTTagCompound;

public interface CompoundMapper<T> {

    T fromCompoundTag(NBTTagCompound tag);

    NBTTagCompound toCompoundTag(T element);
}
