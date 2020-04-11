package fr.dodge.shw.config;

import fr.dodge.shw.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = Reference.MODID, type = Type.INSTANCE, name = Reference.MODID)
public class SHWConfiguration {

    @Comment("Can player use mod commands")
    @Name("Enable Mod")
    @RequiresWorldRestart
    public static boolean ENABLE = true;

    @Comment("Home configuration")
    @Name("Home configuration")
    public static HomeConfiguration HOME = new HomeConfiguration();

    @Comment("Waypoints configuration")
    @Name("Waypoints configuration")
    public static WaypointConfiguration WAYPOINTS = new WaypointConfiguration();

    public static class HomeConfiguration implements ICommandConfiguration {

        @Comment({"Is Home command enable"})
        @Name("Enable")
        @RequiresWorldRestart
        public boolean enable = true;

        @Comment({"Cooldown in second for home command"})
        @Name("Cooldown (s)")
        @RangeInt(min = 0, max = 86400000)
        public int cooldown = 5;

        @Comment({"Player can travel in other dimension using /home", "Example: Nether to OverWorld, End to OverWorld..."})
        @Name("Enable travel through dimension")
        public boolean travelThroughDimension = true;

        @Override
        public int getCooldown() {
            return cooldown;
        }

        @Override
        public void setCooldown(int cooldown) {
            this.cooldown = cooldown;
        }

        @Override
        public boolean isTravelThroughDimension() {
            return travelThroughDimension;
        }

        @Override
        public void setTravelThroughDimension(boolean travelThroughDimension) {
            this.travelThroughDimension = travelThroughDimension;
        }

        @Override
        public int getMaxWaypoints() {
            return 1;
        }

        @Override
        public void setMaxWaypoints(int maxWaypoints) {
            //Nothing to do here
        }
    }

    public static class WaypointConfiguration implements ICommandConfiguration {

        @Comment({"Is Waypoints command enable"})
        @Name("Enable")
        @RequiresWorldRestart
        public boolean enable = true;

        @Comment({"Cooldown in second for waypoints command"})
        @Name("Cooldown (s)")
        @RangeInt(min = 0, max = 86400000)
        public int cooldown = 5;

        @Comment({"Player can travel in other dimension using /wp", "Example: Nether to OverWorld, End to OverWorld..."})
        @Name("Enable travel through dimension")
        public boolean travelThroughDimension = true;

        @Comment({"Number of waypoints per player"})
        @Name("Number of waypoints per player")
        @RangeInt(min = 0, max = 100)
        @SlidingOption
        public int maxWaypoints = 10;

        @Override
        public void setCooldown(int cooldown) {
            this.cooldown = cooldown;
        }

        @Override
        public int getCooldown() {
            return cooldown;
        }

        @Override
        public void setTravelThroughDimension(boolean travelThroughDimension) {
            this.travelThroughDimension = travelThroughDimension;
        }

        @Override
        public boolean isTravelThroughDimension() {
            return travelThroughDimension;
        }

        @Override
        public void setMaxWaypoints(int maxWaypoints) {
            this.maxWaypoints = maxWaypoints;
        }

        @Override
        public int getMaxWaypoints() {
            return maxWaypoints;
        }
        
    }

}
