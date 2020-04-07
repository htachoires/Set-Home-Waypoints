package fr.dodge.shw.command;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.dodge.shw.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SHWWorldSavedData extends WorldSavedData {

	private static final String DATA_NAME = Reference.MODID + "_ExampleData";
	public NBTTagCompound data = new NBTTagCompound();

	public SHWWorldSavedData() {
		super(DATA_NAME);
	}

	public SHWWorldSavedData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		data = nbt;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = data;
		return compound;
	}

	public static void setString(EntityPlayer player, MinecraftServer server, String key, String value) {
		SHWWorldSavedData ex = SHWWorldSavedData.get(server.getWorld(0));
		ex.data.setString(key + player.getUniqueID(), value);
		ex.markDirty();
	}

	public static void setLong(EntityPlayer player, MinecraftServer server, String key, long value) {
		SHWWorldSavedData ex = SHWWorldSavedData.get(server.getWorld(0));
		ex.data.setLong(key + player.getUniqueID(), value);
		ex.markDirty();
	}

	public static String getString(EntityPlayer player, MinecraftServer server, String key) {
		return SHWWorldSavedData.get(server.getWorld(0)).data.getString(key + player.getUniqueID());
	}

	public static Long getLong(EntityPlayer player, MinecraftServer server, String key) {
		return SHWWorldSavedData.get(server.getWorld(0)).data.getLong(key + player.getUniqueID());
	}

	public static Set<String> getData(MinecraftServer server) {
		return SHWWorldSavedData.get(server.getWorld(0)).data.getKeySet();
	}

	public static List<String> getDataOfPlayer(EntityPlayerMP player, MinecraftServer server) {
		return SHWWorldSavedData.get(server.getWorld(0)).data.getKeySet().stream()
				.filter(e -> e.contains(player.getUniqueID().toString()))
				.map(e -> e.replace(player.getUniqueID().toString(), "")).collect(Collectors.toList());
	}

	public static boolean remove(MinecraftServer server, EntityPlayerMP player, String key) {
		SHWWorldSavedData ex = SHWWorldSavedData.get(server.getWorld(0));
		boolean result = ex.data.getKeySet().remove(key + player.getUniqueID());
		ex.markDirty();
		return false;
	}

	public static SHWWorldSavedData get(World world) {
		MapStorage storage = world.getMapStorage();
		SHWWorldSavedData instance = (SHWWorldSavedData) storage.getOrLoadData(SHWWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new SHWWorldSavedData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}

}
