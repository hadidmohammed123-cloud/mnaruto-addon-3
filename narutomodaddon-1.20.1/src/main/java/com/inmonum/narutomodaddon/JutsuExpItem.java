package com.inmonum.narutomodaddon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.narutomod.PlayerTracker;

/** Right-click to consume and gain ninja XP (the old add-on ran addxp2jutsu). */
public class JutsuExpItem extends Item {
    public static final double XP = 10.0D;
    public JutsuExpItem() { super(new Item.Properties().stacksTo(64)); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (!PlayerTracker.isNinja(player)) {
                player.displayClientMessage(Component.literal("You are not a ninja yet."), true);
                return InteractionResultHolder.fail(stack);
            }
            PlayerTracker.addBattleXp(player, XP);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            player.displayClientMessage(Component.literal("+" + (int) XP + " ninja XP (level " + (int) PlayerTracker.getNinjaLevel(player) + ")"), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
