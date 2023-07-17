package com.dodgeman.shw.config;

import com.dodgeman.shw.SetHomeWaypoints;

public class ShwConfigWrapper {
    public static boolean allowHomeToTravelThoughDimension() {
        return SetHomeWaypoints.ShwConfig.allowHomeToTravelThoughDimension.get();
    }

    public static int homeCooldown() {
        return SetHomeWaypoints.ShwConfig.homeCooldown.get();
    }

    public static void setHomeCooldown(int cooldown) {
        SetHomeWaypoints.ShwConfig.homeCooldown.set(cooldown);
    }

    public static void setWaypointsCooldown(int cooldown) {
        SetHomeWaypoints.ShwConfig.waypointsCooldown.set(cooldown);
    }

    public static void setMaximumNumberOfWaypoints(int maxNbOfWaypoints) {
        SetHomeWaypoints.ShwConfig.maximumNumberOfWaypoints.set(maxNbOfWaypoints);
    }

    public static void setAllowWaypointsToTravelThroughDimensionCooldown(boolean travelThroughDimension) {
        SetHomeWaypoints.ShwConfig.allowWaypointsToTravelThoughDimension.set(travelThroughDimension);
    }

    public static void setAllowHomeToTravelThoughDimension(boolean travelThroughDimension) {
        SetHomeWaypoints.ShwConfig.allowHomeToTravelThoughDimension.set(travelThroughDimension);
    }

    public static int waypointsCooldown() {
        return SetHomeWaypoints.ShwConfig.waypointsCooldown.get();
    }

    public static boolean allowWaypointsToTravelThoughDimension() {
        return SetHomeWaypoints.ShwConfig.allowWaypointsToTravelThoughDimension.get();
    }

    public static int maximumNumberOfWaypoints() {
        return SetHomeWaypoints.ShwConfig.maximumNumberOfWaypoints.get();
    }
}
