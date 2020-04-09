package fr.dodge.shw.proxy;

import fr.dodge.shw.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends ServerProxy {

    @SideOnly(Side.CLIENT)
    public static KeyBinding KEY_HOME = new KeyBinding("shw.key.teleport", Keyboard.KEY_P, Reference.NAME);

    @Override
    public void register() {
        ClientRegistry.registerKeyBinding(KEY_HOME);
    }
}
