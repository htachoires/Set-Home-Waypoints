package com.dodgeman.shw.client;

import com.dodgeman.shw.event.KeyBinding;
import com.dodgeman.shw.networking.ModMessage;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;

/**
 * Client-only initialisation, invoked from each loader's client entry point.
 * Registers the go-home key mapping and polls it every client tick.
 */
public class ShwClient {

    public static void init() {
        KeyMappingRegistry.register(KeyBinding.GO_HOME_KEY);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (KeyBinding.GO_HOME_KEY.consumeClick()) {
                ModMessage.sendGoHome();
            }
        });
    }
}
