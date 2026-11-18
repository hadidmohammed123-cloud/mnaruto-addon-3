package com.inmonum.narutomodaddon;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NarutoModAddon.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NarutoModAddon.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarutoModAddon.MODID);

    public static final RegistryObject<MobEffect> CHAKRA_CHARGE_EFFECT = EFFECTS.register("chakra_charge_effect", ChakraChargeEffect::new);
    public static final RegistryObject<MobEffect> CHAKRA_BLOCK = EFFECTS.register("chakra_block_item", ChakraBlockEffect::new);
    public static final RegistryObject<Item> CHAKRA_CHARGE = ITEMS.register("chakra_charge", ChakraChargeItem::new);
    public static final RegistryObject<Item> JUTSU_EXP = ITEMS.register("jutsu_exp", JutsuExpItem::new);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("narutomodaddon", () ->
        CreativeModeTab.builder().title(Component.literal("Naruto Chakra Addon"))
            .icon(() -> CHAKRA_CHARGE.get().getDefaultInstance())
            .displayItems((p, out) -> { out.accept(CHAKRA_CHARGE.get()); out.accept(JUTSU_EXP.get()); })
            .build());
}
