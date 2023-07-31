package com.dodgeman.shw.config;

import com.dodgeman.shw.SetHomeWaypoints;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = SetHomeWaypoints.MOD_ID, type = Type.INSTANCE, name = SetHomeWaypoints.MOD_ID)
public class ShwConfig {

    @Comment({"Cooldown in second before home command can be reused"})
    @Name("/home: Cooldown in seconds")
    @RangeInt(min = 0)
    public static int homeCooldown = 10;

    @Comment({"Is dimensional travel allowed for home command"})
    @Name("/home: Dimensional travel")
    public static boolean isDimensionalTravelAllowedForHome = true;

    @Comment({"Cooldown in second before waypoint command can be reused"})
    @Name("/wp: Cooldown in seconds")
    @RangeInt(min = 0)
    public static int waypointsCooldown = 10;

    @Comment({"Is dimensional travel allowed for waypoints command"})
    @Name("/wp: Dimensional travel")
    public static boolean isDimensionalTravelAllowedForWaypoints = true;

    @Comment({"Number of waypoints per player"})
    @Name("/wp: Maximum number of waypoints")
    @RangeInt(min = 0, max = 100)
    @SlidingOption
    public static int maxNbOfWaypoints = 10;
}
