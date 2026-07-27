package com.dodgeman.shw.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

/**
 * Cross-loader server config, backed by a NightConfig TOML file. NightConfig is the same
 * library NeoForge's ModConfigSpec is built on, so using it directly yields identical TOML
 * semantics on both Fabric and NeoForge with a single implementation.
 *
 * <p>The file is per-world ({@code <world>/serverconfig/shw-server.toml}), matching the
 * original Forge {@code Type.SERVER} location. Autosave flushes each mutation to disk, so the
 * in-game {@code /home config …} and {@code /wp config …} commands take effect immediately.
 */
public class ShwConfig {

    private static final String HOME_DIM_TRAVEL = "shw_is_dimensional_travel_allowed_for_home";
    private static final String HOME_COOLDOWN = "shw_home_cooldown";
    private static final String WP_DIM_TRAVEL = "shw_is_dimensional_travel_allowed_for_waypoints";
    private static final String WP_COOLDOWN = "shw_waypoints_cooldown";
    private static final String MAX_WAYPOINTS = "shw_maximum_number_of_waypoints";

    private static final int MAX_WAYPOINTS_LIMIT = 100;

    private static CommentedFileConfig config;

    /** Loads (or creates with defaults) the per-world server config. */
    public static void load(MinecraftServer server) {
        Path file = server.getWorldPath(LevelResource.ROOT)
                .resolve("serverconfig")
                .resolve("shw-server.toml");
        //noinspection ResultOfMethodCallIgnored
        file.getParent().toFile().mkdirs();

        CommentedFileConfig cfg = CommentedFileConfig.builder(file).sync().autosave().build();
        cfg.load();

        applyDefault(cfg, HOME_COOLDOWN, 10, "Cooldown in second before home command can be reused\nRange: > 0");
        applyDefault(cfg, WP_COOLDOWN, 10, "Cooldown in second before waypoint command can be reused\nRange: > 0");
        applyDefault(cfg, HOME_DIM_TRAVEL, true, "Is dimensional travel allowed for home command");
        applyDefault(cfg, WP_DIM_TRAVEL, true, "Is dimensional travel allowed for waypoints command");
        applyDefault(cfg, MAX_WAYPOINTS, 10, "Maximum number of waypoints by player\nRange: 0 ~ " + MAX_WAYPOINTS_LIMIT);

        cfg.save();
        config = cfg;
    }

    private static void applyDefault(CommentedFileConfig cfg, String key, Object defaultValue, String comment) {
        if (!cfg.contains(key)) {
            cfg.set(key, defaultValue);
        }
        cfg.setComment(key, comment);
    }

    public static boolean isDimensionalTravelAllowedForHome() {
        return config.get(HOME_DIM_TRAVEL);
    }

    public static void setDimensionalTravelAllowedForHome(boolean value) {
        config.set(HOME_DIM_TRAVEL, value);
    }

    public static int getHomeCooldown() {
        return config.getInt(HOME_COOLDOWN);
    }

    public static void setHomeCooldown(int cooldown) {
        config.set(HOME_COOLDOWN, Math.max(0, cooldown));
    }

    public static boolean isDimensionalTravelAllowedForWaypoints() {
        return config.get(WP_DIM_TRAVEL);
    }

    public static void setDimensionalTravelAllowedForWaypoints(boolean value) {
        config.set(WP_DIM_TRAVEL, value);
    }

    public static int getWaypointsCooldown() {
        return config.getInt(WP_COOLDOWN);
    }

    public static void setWaypointsCooldown(int cooldown) {
        config.set(WP_COOLDOWN, Math.max(0, cooldown));
    }

    public static int getMaxNbOfWaypoints() {
        return config.getInt(MAX_WAYPOINTS);
    }

    public static void setMaxNbOfWaypoints(int maxNbOfWaypoints) {
        config.set(MAX_WAYPOINTS, Math.max(0, Math.min(MAX_WAYPOINTS_LIMIT, maxNbOfWaypoints)));
    }
}
