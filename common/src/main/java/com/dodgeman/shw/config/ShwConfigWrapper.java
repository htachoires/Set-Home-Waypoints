package com.dodgeman.shw.config;

/**
 * Stable façade over {@link ShwConfig}. The public API is intentionally unchanged from the
 * original Forge version so every command call site keeps working verbatim.
 */
public class ShwConfigWrapper {
    public static boolean isDimensionalTravelAllowedForHome() {
        return ShwConfig.isDimensionalTravelAllowedForHome();
    }

    public static void setAllowDimensionalTravelForHome(boolean dimensionalTravel) {
        ShwConfig.setDimensionalTravelAllowedForHome(dimensionalTravel);
    }

    public static int getHomeCooldown() {
        return ShwConfig.getHomeCooldown();
    }

    public static void setHomeCooldown(int cooldown) {
        ShwConfig.setHomeCooldown(cooldown);
    }

    public static boolean isDimensionalTravelAllowedForWaypoints() {
        return ShwConfig.isDimensionalTravelAllowedForWaypoints();
    }

    public static void setAllowDimensionalTravelForWaypoints(boolean dimensionalTravel) {
        ShwConfig.setDimensionalTravelAllowedForWaypoints(dimensionalTravel);
    }

    public static int getWaypointsCooldown() {
        return ShwConfig.getWaypointsCooldown();
    }

    public static void setWaypointsCooldown(int cooldown) {
        ShwConfig.setWaypointsCooldown(cooldown);
    }

    public static int getMaxNbOfWaypoints() {
        return ShwConfig.getMaxNbOfWaypoints();
    }

    public static void setMaxNbOfWaypoints(int maxNbOfWaypoints) {
        ShwConfig.setMaxNbOfWaypoints(maxNbOfWaypoints);
    }
}
