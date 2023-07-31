package com.dodgeman.shw.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends ServerProxy {
    public static final String KEY_CATEGORY_GO_HOME = "key.category.shw";
    public static final String KEY_GO_HOME_DESCRIPTION = "key.shw.go_home";
    @SideOnly(Side.CLIENT)
    public static final KeyBinding GO_HOME_KEY = new KeyBinding(KEY_GO_HOME_DESCRIPTION, Keyboard.KEY_H, KEY_CATEGORY_GO_HOME);

    @Override
    public void register() {
        ClientRegistry.registerKeyBinding(GO_HOME_KEY);
    }
}
