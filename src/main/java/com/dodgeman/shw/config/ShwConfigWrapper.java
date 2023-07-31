package com.dodgeman.shw.config;

import com.dodgeman.shw.SetHomeWaypoints;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

public class ShwConfigWrapper {
    public static boolean isDimensionalTravelAllowedForHome() {
        return ShwConfig.isDimensionalTravelAllowedForHome;
    }

    public static void setAllowDimensionalTravelForHome(boolean dimensionalTravel) {
        ShwConfig.isDimensionalTravelAllowedForHome = dimensionalTravel;
    }

    public static int getHomeCooldown() {
        return ShwConfig.homeCooldown;
    }

    public static void setHomeCooldown(int cooldown) {
        ShwConfig.homeCooldown = cooldown;
    }

    public static boolean isDimensionalTravelAllowedForWaypoints() {
        return ShwConfig.isDimensionalTravelAllowedForWaypoints;
    }

    public static void setAllowDimensionalTravelForWaypoints(boolean dimensionalTravel) {
        ShwConfig.isDimensionalTravelAllowedForWaypoints = dimensionalTravel;
    }

    public static int getWaypointsCooldown() {
        return ShwConfig.waypointsCooldown;
    }

    public static void setWaypointsCooldown(int cooldown) {
        ShwConfig.waypointsCooldown = cooldown;
    }

    public static int getMaxNbOfWaypoints() {
        return ShwConfig.maxNbOfWaypoints;
    }

    public static void setMaxNbOfWaypoints(int maxNbOfWaypoints) {
        ShwConfig.maxNbOfWaypoints = maxNbOfWaypoints;
    }

    public static void sync() {
        ConfigManager.sync(SetHomeWaypoints.MOD_ID, Config.Type.INSTANCE);
    }
}
