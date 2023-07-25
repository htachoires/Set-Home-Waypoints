package com.dodgeman.shw.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShwConfig {

    public final ForgeConfigSpec.BooleanValue isDimensionalTravelAllowedForHome;
    public final ForgeConfigSpec.IntValue homeCooldown;
    public final ForgeConfigSpec.BooleanValue isDimensionalTravelAllowedForWaypoints;
    public final ForgeConfigSpec.IntValue waypointsCooldown;
    public final ForgeConfigSpec.IntValue maxNbOfWaypoints;

    public ShwConfig(ForgeConfigSpec.Builder builder) {
        isDimensionalTravelAllowedForHome = builder
                .comment("Is dimensional travel allowed for home command")
                .translation("shw.config.home.dimensionalTravel")
                .define("shw_is_dimensional_travel_allowed_for_home", true);
        homeCooldown = builder
                .comment("Cooldown in second before home command can be reused")
                .translation("shw.config.home.cooldown")
                .defineInRange("shw_home_cooldown", 10, 0, Integer.MAX_VALUE);
        isDimensionalTravelAllowedForWaypoints = builder
                .comment("Is dimensional travel allowed for waypoints command")
                .translation("shw.config.waypoints.dimensionalTravel")
                .define("shw_is_dimensional_travel_allowed_for_waypoints", true);
        waypointsCooldown = builder
                .comment("Cooldown in second before waypoint command can be reused")
                .translation("shw.config.waypoints.cooldown")
                .defineInRange("shw_waypoints_cooldown", 10, 0, Integer.MAX_VALUE);
        maxNbOfWaypoints = builder
                .comment("Maximum number of waypoints by player")
                .translation("shw.config.waypoints.maximumNumberOfWaypoints")
                .defineInRange("shw_maximum_number_of_waypoints", 10, 0, 100);
    }
}
