package com.dodgeman.shw.saveddata.mappers;

import com.dodgeman.shw.saveddata.models.Position;
import com.dodgeman.shw.saveddata.models.Waypoint;
import com.dodgeman.shw.saveddata.models.WaypointName;
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

        if (name.isBlank()) return null;

        Position position = positionCompoundMapper.fromCompoundTag(tag.getCompound(POSITION_KEY));

        return new Waypoint(new WaypointName(name), position);
    }

    @Override
    public CompoundTag toCompoundTag(Waypoint waypoint) {
        CompoundTag tag = new CompoundTag();
        if (waypoint == null) return tag;

        tag.putString(NAME_KEY, waypoint.name().value());
        tag.put(POSITION_KEY, positionCompoundMapper.toCompoundTag(waypoint.position()));

        return tag;
    }
}
