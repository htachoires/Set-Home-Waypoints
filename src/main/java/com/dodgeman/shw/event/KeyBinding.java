package com.dodgeman.shw.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_GO_HOME = "key.category.shw";
    public static final String KEY_GO_HOME = "key.shw.go_home";

    public static final KeyMapping GO_HOME_KEY = new KeyMapping(KEY_GO_HOME, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY_GO_HOME);
}
