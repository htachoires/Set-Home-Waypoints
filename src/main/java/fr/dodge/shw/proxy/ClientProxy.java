package fr.dodge.shw.proxy;

import fr.dodge.shw.network.HomeEventHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends ServerProxy {

	@Override
	public void register() {
		ClientRegistry.registerKeyBinding(HomeEventHandler.keyHome);
	}
}
