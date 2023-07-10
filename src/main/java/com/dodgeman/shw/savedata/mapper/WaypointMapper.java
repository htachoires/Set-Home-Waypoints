package com.dodgeman.shw.savedata.mapper;

import com.dodgeman.shw.savedata.model.Position;
import com.dodgeman.shw.savedata.model.Waypoint;
import net.minecraft.nbt.CompoundTag;

public class WaypointMapper implements CompoundMapper<Waypoint> {

    private static final String NAME_KEY = "name";
    private static final String POSITION_KEY = "position";

    private final CompoundMapper<Position> positionCompoundMapper;

    public WaypointMapper() {
        positionCompoundMapper = new PositionMapper();
    }

    @Override
    public Waypoint fromCompoundTag(CompoundTag tag) {
        String name = tag.getString(NAME_KEY);

        Position position = positionCompoundMapper.fromCompoundTag(tag.getCompound(POSITION_KEY));

        return new Waypoint(name, position);
    }

    @Override
    public CompoundTag toCompoundTag(Waypoint waypoint) {
        CompoundTag tag = new CompoundTag();
        if (waypoint == null) return tag;

        tag.putString(NAME_KEY, waypoint.name());
        tag.put(POSITION_KEY, positionCompoundMapper.toCompoundTag(waypoint.position()));

        return tag;
    }
}
