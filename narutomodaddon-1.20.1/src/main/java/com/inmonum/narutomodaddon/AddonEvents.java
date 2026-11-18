package com.inmonum.narutomodaddon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.narutomod.PlayerTracker;

public class AddonEvents {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer p && AddonConfig.NINJA_START.get()) AddonCommands.startNinja(p);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent e) {
        if (AddonConfig.KEEP_CHAKRA.get()) setKeepChakra(e.getServer(), true);
    }

    public static void setKeepChakra(MinecraftServer s, boolean v) {
        s.getGameRules().getRule(PlayerTracker.KEEP_NINJA_XP).set(v, s);
    }
}
