package com.dodgeman.shw.event;

import com.dodgeman.shw.SetHomeWaypoints;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Client-only key mapping. Loaded exclusively from the client entry points, never on a
 * dedicated server. The Forge-specific {@code KeyConflictContext} has no Architectury
 * equivalent and is dropped (default conflict context).
 */
public class KeyBinding {
    public static final String KEY_GO_HOME = "key.shw.go_home";

    // Since MC 26.x, key mapping categories are registered objects (KeyMapping.Category)
    // keyed by an Identifier, replacing the old free-form translation-key string. The label
    // is looked up under the "key.categories.<namespace>.<path>" translation key.
    public static final KeyMapping.Category KEY_CATEGORY_GO_HOME =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(SetHomeWaypoints.MOD_ID, "main"));

    public static final KeyMapping GO_HOME_KEY = new KeyMapping(KEY_GO_HOME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY_GO_HOME);
}
