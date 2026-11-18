package com.inmonum.narutomodaddon;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.narutomod.Chakra;

/** While active, refills the holder's ShinobiCraft chakra every tick; removes itself once full. */
public class ChakraChargeEffect extends MobEffect {
    public ChakraChargeEffect() { super(MobEffectCategory.BENEFICIAL, 0x2D9CFF); }

    @Override public boolean isDurationEffectTick(int duration, int amplifier) { return true; }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide || !(entity instanceof ServerPlayer player)) return;
        Chakra.Pathway path = Chakra.pathway(player);
        if (path == null) return;
        if (path.isFull()) {
            player.removeEffect(this);
            player.displayClientMessage(Component.literal("Max Chakra"), true);
            return;
        }
        // regenerate faster at higher amplifier (old add-on used regeneration levels 1-3)
        double step = path.getMax() * 0.002D * (1 + Math.min(amplifier, 2));
        path.set(Math.min(path.getMax(), path.getAmount() + step));
    }
}
