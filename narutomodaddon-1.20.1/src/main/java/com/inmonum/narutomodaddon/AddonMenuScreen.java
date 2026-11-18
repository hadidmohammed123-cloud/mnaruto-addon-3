package com.inmonum.narutomodaddon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Locale;

/** Simple menu: pick a kekkei genkai, start as a ninja, or close. Each button runs the matching command. */
public class AddonMenuScreen extends Screen {
    public AddonMenuScreen() { super(Component.literal("Naruto Chakra Addon")); }

    @Override
    protected void init() {
        int cols = 3, bw = 100, bh = 20, gap = 4;
        int x0 = this.width / 2 - (cols * bw + (cols - 1) * gap) / 2;
        int y0 = 50;
        int i = 0;
        for (String k : KkgData.MAP.keySet()) {
            int cx = x0 + (i % cols) * (bw + gap), cy = y0 + (i / cols) * (bh + gap);
            this.addRenderableWidget(Button.builder(Component.literal(cap(k)), b -> run("changekkg " + k))
                .bounds(cx, cy, bw, bh).build());
            i++;
        }
        int rows = (i + cols - 1) / cols;
        int y = y0 + rows * (bh + gap) + 10;
        this.addRenderableWidget(Button.builder(Component.literal("Start as Ninja"), b -> run("ninjastart"))
            .bounds(this.width / 2 - bw - 2, y, bw, bh).build());
        this.addRenderableWidget(Button.builder(Component.literal("Close"), b -> this.onClose())
            .bounds(this.width / 2 + 2, y, bw, bh).build());
    }

    private void run(String cmd) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) mc.player.connection.sendCommand(cmd);
        this.onClose();
    }

    private static String cap(String s) { return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1); }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        this.renderBackground(g);
        g.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        g.drawCenteredString(this.font, "Pick a kekkei genkai", this.width / 2, 34, 0xAAAAAA);
        super.render(g, mx, my, pt);
    }
}
