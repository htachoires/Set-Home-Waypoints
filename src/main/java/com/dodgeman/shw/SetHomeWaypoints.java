package com.dodgeman.shw;

import com.dodgeman.shw.client.commands.HomeCommand;
import com.dodgeman.shw.client.commands.SetHomeCommand;
import com.dodgeman.shw.client.commands.ShwCommand;
import com.dodgeman.shw.client.commands.WaypointsCommand;
import com.dodgeman.shw.config.ShwConfig;
import com.dodgeman.shw.event.KeyBinding;
import com.dodgeman.shw.networking.ModMessage;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

@Mod(SetHomeWaypoints.MOD_ID)
public class SetHomeWaypoints {

    public static final String MOD_ID = "shw";
    public static final ShwConfig ShwConfig;
    private static final ForgeConfigSpec SHW_CONFIG_SPEC;

    static {
        final Pair<ShwConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ShwConfig::new);
        ShwConfig = specPair.getLeft();
        SHW_CONFIG_SPEC = specPair.getRight();
    }

    public SetHomeWaypoints() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SHW_CONFIG_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(KeyBinding.GO_HOME_KEY);
    }

    @SubscribeEvent
    public void onRegisterCommands(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        HomeCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        WaypointsCommand.register(dispatcher);
        ShwCommand.register(dispatcher);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessage.register();
    }
}
