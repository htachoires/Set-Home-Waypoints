package fr.dodge.shw.proxy;

import org.lwjgl.input.Keyboard;

import fr.dodge.shw.Reference;
import fr.dodge.shw.client.HomeEventHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends ServerProxy {

	public static KeyBinding keyHome = new KeyBinding("Teleport to your home", Keyboard.KEY_P, Reference.NAME);

	@Override
	public void register() {
		ClientRegistry.registerKeyBinding(keyHome);
	}
}
