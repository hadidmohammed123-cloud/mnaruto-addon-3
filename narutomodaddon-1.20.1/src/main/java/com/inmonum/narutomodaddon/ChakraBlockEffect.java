package com.inmonum.narutomodaddon;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** While active the player cannot charge chakra with the charge key. */
public class ChakraBlockEffect extends MobEffect {
    public ChakraBlockEffect() { super(MobEffectCategory.HARMFUL, 0xAA0000); }
}
