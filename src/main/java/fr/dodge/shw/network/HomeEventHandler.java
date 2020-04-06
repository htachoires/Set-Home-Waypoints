package fr.dodge.shw.network;

import org.lwjgl.input.Keyboard;

import fr.dodge.shw.Reference;
import fr.dodge.shw.SetHomeWaypoints;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HomeEventHandler {

	public static KeyBinding keyHome = new KeyBinding("Teleport to your home", Keyboard.KEY_P, Reference.NAME);

	@SubscribeEvent
	public static void onKeyPressed(KeyInputEvent event) throws CommandException {
		if (keyHome.isPressed()) {
			SHWPacketHandler.INSTANCE.sendToServer(new MyMessage(64));
		}
	}
}
