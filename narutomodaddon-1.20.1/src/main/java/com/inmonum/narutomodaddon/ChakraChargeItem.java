package com.inmonum.narutomodaddon;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChakraChargeItem extends Item {
    public ChakraChargeItem() { super(new Item.Properties().stacksTo(1)); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            // toggle: right-click starts charging, right-click again stops
            if (player.hasEffect(AddonRegistry.CHAKRA_CHARGE_EFFECT.get())) player.removeEffect(AddonRegistry.CHAKRA_CHARGE_EFFECT.get());
            else player.addEffect(new MobEffectInstance(AddonRegistry.CHAKRA_CHARGE_EFFECT.get(), 20 * 60, 0, false, false, true));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
