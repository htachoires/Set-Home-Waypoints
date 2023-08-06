package com.dodgeman.shw.config;

import com.dodgeman.shw.SetHomeWaypoints;

public class ShwConfigWrapper {
    public static boolean isDimensionalTravelAllowedForHome() {
        return SetHomeWaypoints.ShwConfig.isDimensionalTravelAllowedForHome.get();
    }

    public static void setAllowDimensionalTravelForHome(boolean dimensionalTravel) {
        SetHomeWaypoints.ShwConfig.isDimensionalTravelAllowedForHome.set(dimensionalTravel);
    }

    public static int getHomeCooldown() {
        return SetHomeWaypoints.ShwConfig.homeCooldown.get();
    }

    public static void setHomeCooldown(int cooldown) {
        SetHomeWaypoints.ShwConfig.homeCooldown.set(cooldown);
    }

    public static boolean isDimensionalTravelAllowedForWaypoints() {
        return SetHomeWaypoints.ShwConfig.isDimensionalTravelAllowedForWaypoints.get();
    }

    public static void setAllowDimensionalTravelForWaypoints(boolean dimensionalTravel) {
        SetHomeWaypoints.ShwConfig.isDimensionalTravelAllowedForWaypoints.set(dimensionalTravel);
    }

    public static int getWaypointsCooldown() {
        return SetHomeWaypoints.ShwConfig.waypointsCooldown.get();
    }

    public static void setWaypointsCooldown(int cooldown) {
        SetHomeWaypoints.ShwConfig.waypointsCooldown.set(cooldown);
    }

    public static int getMaxNbOfWaypoints() {
        return SetHomeWaypoints.ShwConfig.maxNbOfWaypoints.get();
    }

    public static void setMaxNbOfWaypoints(int maxNbOfWaypoints) {
        SetHomeWaypoints.ShwConfig.maxNbOfWaypoints.set(maxNbOfWaypoints);
    }
}
