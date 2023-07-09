package com.dodgeman.shw.savedata;

import com.dodgeman.shw.SetHomeWaypoints;
import com.dodgeman.shw.savedata.mapper.CompoundMapper;
import com.dodgeman.shw.savedata.mapper.PlayerHomeAndWaypointsMapper;
import com.dodgeman.shw.savedata.model.PlayerHomeAndWaypoints;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.UUID;

public class SetHomeWaypointsSavedDataFactory {

    private final CompoundMapper<PlayerHomeAndWaypoints> playerHomeAndWaypointsMapper;

    public SetHomeWaypointsSavedDataFactory() {
        this.playerHomeAndWaypointsMapper = new PlayerHomeAndWaypointsMapper();
    }

    public SetHomeAndWaypointsSavedData createAndLoad() {
        DimensionDataStorage dataStorage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();

        return dataStorage.computeIfAbsent(this::load, SetHomeAndWaypointsSavedData::new, SetHomeWaypoints.MODID);
    }

    public SetHomeAndWaypointsSavedData load(CompoundTag compoundTag) {
        CompoundTag rawPlayerHomeAndWaypoints = compoundTag.getCompound(SetHomeWaypoints.MODID);

        HashMap<UUID, PlayerHomeAndWaypoints> playersHomeAndWaypoints = new HashMap<>();

        rawPlayerHomeAndWaypoints
                .getAllKeys()
                .forEach(uuid -> playersHomeAndWaypoints.put(
                        UUID.fromString(uuid),
                        playerHomeAndWaypointsMapper.fromCompoundTag(rawPlayerHomeAndWaypoints.getCompound(uuid))));

        return new SetHomeAndWaypointsSavedData(playersHomeAndWaypoints);
    }
}
