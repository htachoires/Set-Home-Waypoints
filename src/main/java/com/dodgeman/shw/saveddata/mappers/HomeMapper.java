package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Home;
import com.dodgeman.shw.saveddata.models.Position;
import net.minecraft.nbt.NBTTagCompound;

public class HomeMapper implements CompoundMapper<Home> {

    private static final String POSITION_KEY = "position";

    private final CompoundMapper<Position> positionCompoundMapper;

    public HomeMapper() {
        positionCompoundMapper = new PositionMapper();
    }

    @Override
    public Home fromCompoundTag(NBTTagCompound tag) {
        Position position = positionCompoundMapper.fromCompoundTag(tag.getCompoundTag(POSITION_KEY));
        return new Home(position);
    }

    @Override
    public NBTTagCompound toCompoundTag(Home home) {
        NBTTagCompound tag = new NBTTagCompound();
        if (home == null) return tag;

        tag.setTag(POSITION_KEY, positionCompoundMapper.toCompoundTag(home.position()));
        return tag;
    }
}
