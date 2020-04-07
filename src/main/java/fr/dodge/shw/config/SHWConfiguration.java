package fr.dodge.shw.config;

import fr.dodge.shw.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.SlidingOption;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = Reference.MODID, type = Type.INSTANCE, name = Reference.MODID)
public class SHWConfiguration {

	@Comment("Can player use mod commands")
	@Name("Enable Mod")
	@RequiresWorldRestart
	public static boolean ENABLE = true;

	@Comment("Home configuration")
	@Name("Home configuration")
	public static HomeConfiguration HOME_CONFIG = new HomeConfiguration();

	@Comment("Waypoints configuration")
	@Name("Waypoints configuration")
	public static WayPointConfiguration WAYPOINTS_CONFIG = new WayPointConfiguration();

	public static class HomeConfiguration {

		@Comment({ "Is Home command enable" })
		@Name("Enable")
		@RequiresWorldRestart
		public boolean ENABLE = true;

		@Comment({ "After a player die he will lose his home set... homeless" })
		@Name("Keep home set after death")
		public boolean KEEP_HOME_AFTER_DEATH = true;

		@Comment({ "Cooldown in millis for home command" })
		@Name("Cooldown (ms)")
		@RangeInt(min = 0, max = 86400000)
		public int COOLDOWN = 5000;

	}

	public static class WayPointConfiguration {

		@Comment({ "Is Waypoints command enable" })
		@Name("Enable")
		@RequiresWorldRestart
		public boolean ENABLE = true;

		@Comment({ "After a player die he will lose hiw waypoints...pouf" })
		@Name("Keep waypoints after death")
		public boolean KEEP_WAYPOINTS_AFTER_DEATH = true;

		@Comment({ "Cooldown in millis for waypoints command" })
		@Name("Cooldown (ms)")
		@RangeInt(min = 0, max = 86400000)
		public int COOLDOWN = 5000;

		@Comment({ "Number of waypoints per player" })
		@Name("Number of waypoints per player")
		@RangeInt(min = 0, max = 100)
		@SlidingOption
		public int MAX_WAYPOINTS = 10;

	}

}
