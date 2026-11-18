package com.inmonum.narutomodaddon;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NarutoModAddon.MODID, value = Dist.CLIENT)
public class ClientTick {
    private static boolean down = false;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) { down = false; return; }
        boolean now = ClientKeys.CHARGE.isDown() && mc.screen == null;
        if (now != down) {
            down = now;
            AddonNetwork.CHANNEL.sendToServer(new ChargePacket(now));
        }
        while (ClientKeys.MENU.consumeClick()) mc.setScreen(new AddonMenuScreen());
    }
}
