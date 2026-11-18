package com.inmonum.narutomodaddon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.narutomod.Chakra;

public class ChargeLogic {
    public static void set(ServerPlayer p, boolean start) {
        if (start) {
            if (p.hasEffect(AddonRegistry.CHAKRA_BLOCK.get())) {
                msg(p, "..Charging Blocked..", ChatFormatting.RED);
                return;
            }
            Chakra.Pathway path = Chakra.pathway(p);
            if (path == null) return;
            if (path.isFull()) { msg(p, "Max Chakra", ChatFormatting.BLUE); return; }
            p.addEffect(new MobEffectInstance(AddonRegistry.CHAKRA_CHARGE_EFFECT.get(), 20 * 600, 0, false, false, true));
            p.level().playSound(null, p.blockPosition(), SoundEvents.SHULKER_SHOOT, SoundSource.NEUTRAL, 0.6F, 1.0F);
            msg(p, "Charging Chakra", ChatFormatting.DARK_AQUA);
        } else if (p.hasEffect(AddonRegistry.CHAKRA_CHARGE_EFFECT.get())) {
            p.removeEffect(AddonRegistry.CHAKRA_CHARGE_EFFECT.get());
            msg(p, "Charging Stopped", ChatFormatting.RED);
        }
    }

    private static void msg(ServerPlayer p, String s, ChatFormatting color) {
        p.displayClientMessage(Component.literal(s).withStyle(color, ChatFormatting.BOLD), true);
    }
}
