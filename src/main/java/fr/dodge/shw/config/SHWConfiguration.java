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
    public static WayPointConfiguration WAYPOINTS = new WayPointConfiguration();

    public static class HomeConfiguration {

        @Comment({"Is Home command enable"})
        @Name("Enable")
        @RequiresWorldRestart
        public boolean ENABLE = true;

        @Comment({"Cooldown in second for home command"})
        @Name("Cooldown (s)")
        @RangeInt(min = 0, max = 86400000)
        public int COOLDOWN = 5;

    }

    public static class WayPointConfiguration {

        @Comment({"Is Waypoints command enable"})
        @Name("Enable")
        @RequiresWorldRestart
        public boolean ENABLE = true;

        @Comment({"Cooldown in second for waypoints command"})
        @Name("Cooldown (s)")
        @RangeInt(min = 0, max = 86400000)
        public int COOLDOWN = 5;

        @Comment({"Number of waypoints per player"})
        @Name("Number of waypoints per player")
        @RangeInt(min = 0, max = 100)
        @SlidingOption
        public int MAX_WAYPOINTS = 10;

    }

}
