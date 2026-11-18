package com.inmonum.narutomodaddon;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(NarutoModAddon.MODID)
public class NarutoModAddon {
    public static final String MODID = "narutomodaddon";

    public NarutoModAddon() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        AddonRegistry.EFFECTS.register(bus);
        AddonRegistry.ITEMS.register(bus);
        AddonRegistry.TABS.register(bus);
        MinecraftForge.EVENT_BUS.register(AddonCommands.class);
    }
}
