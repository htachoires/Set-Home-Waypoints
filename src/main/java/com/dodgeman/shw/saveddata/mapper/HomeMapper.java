package com.dodgeman.shw.saveddata.mapper;

import com.dodgeman.shw.saveddata.model.Home;
import com.dodgeman.shw.saveddata.model.Position;
import net.minecraft.nbt.CompoundTag;

public class HomeMapper implements CompoundMapper<Home> {

    private static final String POSITION_KEY = "position";

    private final CompoundMapper<Position> positionCompoundMapper;

    public HomeMapper() {
        positionCompoundMapper = new PositionMapper();
    }

    @Override
    public Home fromCompoundTag(CompoundTag tag) {
        Position position = positionCompoundMapper.fromCompoundTag(tag.getCompound(POSITION_KEY));
        return new Home(position);
    }

    @Override
    public CompoundTag toCompoundTag(Home home) {
        CompoundTag tag = new CompoundTag();
        if (home == null) return tag;

        tag.put(POSITION_KEY, positionCompoundMapper.toCompoundTag(home.position()));
        return tag;
    }
}
