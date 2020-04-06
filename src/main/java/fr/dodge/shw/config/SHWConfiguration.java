package fr.dodge.shw.config;

import java.io.File;

import fr.dodge.shw.Reference;
import fr.dodge.shw.SetHomeWaypoints;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, type = Type.INSTANCE, name = Reference.MODID)
public class SHWConfiguration {
	
	@Comment("Can player use mod commands")
	@Name("Enable Mod")
	public static boolean ENABLE = true;
	
	@Comment("Home configuration")
	@Name("Home configuration")
	public static HomeConfiguration homeConfig = new HomeConfiguration();
	
	@Comment("Waypoints configuration")
	@Name("Waypoints configuration")
	public static WayPointConfiguration waypointsConfig = new WayPointConfiguration();
	
	public static class HomeConfiguration {

		@Comment({ "Is Home command enable" })
		@Name("Enable")
		public boolean ENABLE = true;

		@Comment({ "Cooldown in millis for home command" })
		@Name("Cooldown (ms)")
		@RangeInt(min = 0, max = 86400000)
		public int COOLDOWN = 5000;

	}
	
	public static class WayPointConfiguration {

		@Comment({ "Is Waypoints command enable" })
		@Name("Enable")
		public boolean ENABLE = true;

		@Comment({ "Cooldown in millis for waypoints command" })
		@Name("Cooldown (ms)")
		@RangeInt(min = 0, max = 86400000)
		public int COOLDOWN = 5000;

		@Comment({ "Number of waypoints you can have" })
		@Name("Maximum")
		@RangeInt(min = 0, max = 100)
		public int MAX_WAYPOINTS = 10;
	}
}
