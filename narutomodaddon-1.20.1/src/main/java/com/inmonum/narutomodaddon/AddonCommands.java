package com.inmonum.narutomodaddon;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.narutomod.Chakra;
import net.narutomod.PlayerTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddonCommands {
    private static final SuggestionProvider<CommandSourceStack> KKG_SUGGEST =
        (c, b) -> SharedSuggestionProvider.suggest(KkgData.MAP.keySet(), b);

    @SubscribeEvent
    public static void register(RegisterCommandsEvent e) {
        // /addchakra <player> <amount>  -> adds ninja XP
        e.getDispatcher().register(Commands.literal("addchakra").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player", EntityArgument.player())
            .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
            .executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player");
                double a = DoubleArgumentType.getDouble(c, "amount");
                PlayerTracker.addBattleXp(p, a);
                c.getSource().sendSuccess(() -> Component.literal("Added " + a + " ninja XP to " + p.getName().getString()), true);
                return 1;
            }))));

        // /fillchakra <player>
        e.getDispatcher().register(Commands.literal("fillchakra").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player", EntityArgument.player()).executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player");
                Chakra.Pathway path = Chakra.pathway(p);
                if (path == null) return 0;
                path.set(path.getMax());
                return 1;
            })));

        // /ninjastart  (yourself, if you are not a ninja yet)   /ninjastart for <player> (ops)
        e.getDispatcher().register(Commands.literal("ninjastart")
            .executes(c -> {
                ServerPlayer p = c.getSource().getPlayerOrException();
                if (!startNinja(p)) { c.getSource().sendFailure(Component.literal("You are already a ninja.")); return 0; }
                c.getSource().sendSuccess(() -> Component.literal("You are now a ninja."), false);
                return 1;
            })
            .then(Commands.literal("for").requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player()).executes(c -> {
                    ServerPlayer p = EntityArgument.getPlayer(c, "player");
                    if (!startNinja(p)) { c.getSource().sendFailure(Component.literal(p.getName().getString() + " is already a ninja.")); return 0; }
                    c.getSource().sendSuccess(() -> Component.literal(p.getName().getString() + " is now a ninja."), true);
                    return 1;
                }))));

        // /changekkg <kkg>   /changekkg for <player> <kkg> (ops)
        e.getDispatcher().register(Commands.literal("changekkg")
            .then(Commands.argument("kkg", StringArgumentType.word()).suggests(KKG_SUGGEST).executes(c -> {
                if (!AddonConfig.REROLL_KKG.get() && !c.getSource().hasPermission(2)) {
                    c.getSource().sendFailure(Component.literal("Changing kekkei genkai is disabled on this server."));
                    return 0;
                }
                return change(c, c.getSource().getPlayerOrException());
            }))
            .then(Commands.literal("for").requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("kkg", StringArgumentType.word()).suggests(KKG_SUGGEST)
                    .executes(c -> change(c, EntityArgument.getPlayer(c, "player")))))));

        // /addonsettings [ninjastart|rerollkkg|keepchakra <true|false>]  (ops)
        e.getDispatcher().register(Commands.literal("addonsettings").requires(s -> s.hasPermission(2))
            .executes(c -> {
                c.getSource().sendSuccess(() -> Component.literal(
                    "Ninja Start: " + AddonConfig.NINJA_START.get()
                    + " | Reroll Kekkei Genkai: " + AddonConfig.REROLL_KKG.get()
                    + " | Keep Chakra on Death: " + AddonConfig.KEEP_CHAKRA.get()), false);
                return 1;
            })
            .then(Commands.literal("ninjastart").then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> {
                AddonConfig.NINJA_START.set(BoolArgumentType.getBool(c, "value"));
                c.getSource().sendSuccess(() -> Component.literal("Ninja Start: " + AddonConfig.NINJA_START.get()), true);
                return 1;
            })))
            .then(Commands.literal("rerollkkg").then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> {
                AddonConfig.REROLL_KKG.set(BoolArgumentType.getBool(c, "value"));
                c.getSource().sendSuccess(() -> Component.literal("Reroll Kekkei Genkai: " + AddonConfig.REROLL_KKG.get()), true);
                return 1;
            })))
            .then(Commands.literal("keepchakra").then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> {
                boolean v = BoolArgumentType.getBool(c, "value");
                AddonConfig.KEEP_CHAKRA.set(v);
                AddonEvents.setKeepChakra(c.getSource().getServer(), v);
                c.getSource().sendSuccess(() -> Component.literal("Keep Chakra on Death: " + v), true);
                return 1;
            }))));
    }

    private static int change(CommandContext<CommandSourceStack> c, ServerPlayer target) {
        String k = StringArgumentType.getString(c, "kkg").toLowerCase(Locale.ROOT);
        if (!KkgData.MAP.containsKey(k)) {
            c.getSource().sendFailure(Component.literal("Unknown kekkei genkai: " + k));
            return 0;
        }
        changeKkg(target, k);
        c.getSource().sendSuccess(() -> Component.literal("Set " + target.getName().getString() + "'s kekkei genkai to " + k), true);
        return 1;
    }

    public static boolean startNinja(ServerPlayer p) {
        if (PlayerTracker.isNinja(p)) return false;
        award(p, new ResourceLocation("narutomod", "ninjaachievement"));
        PlayerTracker.addBattleXp(p, AddonConfig.NINJA_START_XP.get());
        return true;
    }

    public static void changeKkg(ServerPlayer p, String name) {
        KkgData.Entry chosen = KkgData.MAP.get(name);
        for (KkgData.Entry e : KkgData.MAP.values())
            if (e.adv() != null) revoke(p, new ResourceLocation("narutomod", e.adv()));
        p.getInventory().clearOrCountMatchingItems(KkgData::isKkgItem, -1, p.inventoryMenu.getCraftSlots());
        if (chosen.adv() != null) award(p, new ResourceLocation("narutomod", chosen.adv()));
        if (chosen.item() != null) {
            Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation("narutomod", chosen.item()));
            if (it != null && it != Items.AIR) {
                ItemStack st = new ItemStack(it);
                CompoundTag tag = st.getOrCreateTag();
                tag.putUUID("OwnerIdKey", p.getUUID());
                if (chosen.nature()) tag.putBoolean("IsNatureAffinityKey", true);
                if (!p.getInventory().add(st)) p.drop(st, false);
            }
        }
        p.containerMenu.broadcastChanges();
    }

    private static void award(ServerPlayer p, ResourceLocation id) {
        Advancement a = p.server.getAdvancements().getAdvancement(id);
        if (a == null) return;
        AdvancementProgress pr = p.getAdvancements().getOrStartProgress(a);
        List<String> list = new ArrayList<>();
        pr.getRemainingCriteria().forEach(list::add);
        for (String s : list) p.getAdvancements().award(a, s);
    }

    private static void revoke(ServerPlayer p, ResourceLocation id) {
        Advancement a = p.server.getAdvancements().getAdvancement(id);
        if (a == null) return;
        AdvancementProgress pr = p.getAdvancements().getOrStartProgress(a);
        List<String> list = new ArrayList<>();
        pr.getCompletedCriteria().forEach(list::add);
        for (String s : list) p.getAdvancements().revoke(a, s);
    }
}
