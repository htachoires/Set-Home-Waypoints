package com.dodgeman.shw.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * Client-only key mapping. Loaded exclusively from the client entry points, never on a
 * dedicated server. The Forge-specific {@code KeyConflictContext} has no Architectury
 * equivalent and is dropped (default conflict context).
 */
public class KeyBinding {
    public static final String KEY_CATEGORY_GO_HOME = "key.category.shw";
    public static final String KEY_GO_HOME = "key.shw.go_home";

    public static final KeyMapping GO_HOME_KEY = new KeyMapping(KEY_GO_HOME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY_GO_HOME);
}
