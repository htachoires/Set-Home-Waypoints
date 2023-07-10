package com.dodgeman.shw.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShwConfig {

    public final ForgeConfigSpec.BooleanValue allowHomeToTravelThoughDimension;
    public final ForgeConfigSpec.IntValue homeCooldown;
    public final ForgeConfigSpec.BooleanValue allowWaypointsToTravelThoughDimension;
    public final ForgeConfigSpec.IntValue waypointsCooldown;
    public final ForgeConfigSpec.IntValue maximumNumberOfWaypoints;

    public ShwConfig(ForgeConfigSpec.Builder builder) {
        allowHomeToTravelThoughDimension = builder
                .comment("Allow home command to travel through dimension")
                .translation("shw.config.home.allowTravelThroughDimension")
                .define("shw_home_allow_travel_through_dimension", true);
        homeCooldown = builder
                .comment("Cooldown in second before home command can be reused")
                .translation("shw.config.home.cooldown")
                .defineInRange("shw_home_cooldown", 10, 0, Integer.MAX_VALUE);

        allowWaypointsToTravelThoughDimension = builder
                .comment("Allow waypoints command to travel through dimension")
                .translation("shw.config.waypoints.allowTravelThroughDimension")
                .define("shw_waypoints_allow_travel_through_dimension", true);
        waypointsCooldown = builder
                .comment("Cooldown in second before waypoint command can be reused")
                .translation("shw.config.waypoints.cooldown")
                .defineInRange("shw_waypoints_cooldown", 10, 0, Integer.MAX_VALUE);
        maximumNumberOfWaypoints = builder
                .comment("Maximum number of waypoints by player")
                .translation("shw.config.waypoints.maximumNumberOfWaypoints")
                .defineInRange("shw_waypoints_maximum_number_of_waypoints", 10, 0, 10);
    }
}
