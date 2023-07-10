package com.dodgeman.shw.config;

import com.dodgeman.shw.SetHomeWaypoints;

public class ShwConfigWrapper {
    public static boolean allowHomeToTravelThoughDimension() {
        return SetHomeWaypoints.ShwConfig.allowHomeToTravelThoughDimension.get();
    }

    public static int homeCooldown() {
        return SetHomeWaypoints.ShwConfig.homeCooldown.get();
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
