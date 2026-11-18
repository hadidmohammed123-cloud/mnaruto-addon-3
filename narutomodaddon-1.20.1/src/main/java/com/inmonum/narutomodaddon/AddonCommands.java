package com.inmonum.narutomodaddon;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.narutomod.Chakra;
import net.narutomod.PlayerTracker;

public class AddonCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent e) {
        // /addchakra <player> <amount>  -> adds ninja XP (same as the 1.12.2 add-on)
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
        // /fillchakra <player> -> sets chakra to max
        e.getDispatcher().register(Commands.literal("fillchakra").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player", EntityArgument.player()).executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player");
                Chakra.Pathway path = Chakra.pathway(p);
                if (path == null) return 0;
                path.set(path.getMax());
                return 1;
            })));
    }
}
