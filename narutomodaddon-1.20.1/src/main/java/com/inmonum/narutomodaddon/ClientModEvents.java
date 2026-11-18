package com.inmonum.narutomodaddon;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.narutomod.Chakra;
import net.minecraft.client.gui.GuiGraphics;

@Mod.EventBusSubscriber(modid = NarutoModAddon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent e) {
        e.register(ClientKeys.CHARGE);
        e.register(ClientKeys.MENU);
    }

    @SubscribeEvent
    public static void overlays(RegisterGuiOverlaysEvent e) {
        e.registerAboveAll("chakra_status", ClientModEvents::render);
    }

    private static void render(ForgeGui gui, GuiGraphics g, float partialTick, int w, int h) {
        Minecraft mc = Minecraft.getInstance();
        Player p = mc.player;
        if (p == null || mc.options.hideGui) return;
        boolean charging = p.hasEffect(AddonRegistry.CHAKRA_CHARGE_EFFECT.get());
        boolean blocked = p.hasEffect(AddonRegistry.CHAKRA_BLOCK.get());
        if (!charging && !blocked) return;
        if (blocked) g.drawCenteredString(mc.font, "Chakra Blocked", w / 2, h - 75, 0xFF5555);
        if (charging) {
            g.drawCenteredString(mc.font, "Charging Chakra", w / 2, h - 75, 0x55AAFF);
            Chakra.Pathway path = Chakra.pathway(p);
            if (path != null && path.getMax() > 0) {
                int x = w / 2 - 50, y = h - 62;
                int filled = (int) Math.max(0, Math.min(100, 100.0D * path.getAmount() / path.getMax()));
                g.fill(x - 1, y - 1, x + 101, y + 6, 0xFF000000);
                g.fill(x, y, x + filled, y + 5, 0xFF2D9CFF);
            }
        }
    }
}
