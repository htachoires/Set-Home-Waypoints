package fr.dodge.shw.client;

import java.util.Set;
import org.lwjgl.input.Keyboard;

import fr.dodge.shw.Reference;
import fr.dodge.shw.SetHomeWaypoints;
import fr.dodge.shw.network.MyMessage;
import fr.dodge.shw.network.SHWPacketHandler;
import fr.dodge.shw.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.Config;

public class SHWEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressed(KeyInputEvent event) {
		if (ClientProxy.keyHome.isPressed()) {
			SHWPacketHandler.INSTANCE.sendToServer(new MyMessage());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressed(EntityConstructing event) {
		if (event.getEntity().equals(Minecraft.getMinecraft().player)) {
			SHWPacketHandler.INSTANCE.sendToServer(new MyMessage());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressed(PostConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyPressed(LivingDeathEvent event) {
		if (event.getEntity().equals(Minecraft.getMinecraft().player)) {
			SHWPacketHandler.INSTANCE.sendToServer(new MyMessage());
		}
	}
}
