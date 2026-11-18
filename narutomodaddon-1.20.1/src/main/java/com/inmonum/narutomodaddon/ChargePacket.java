package com.inmonum.narutomodaddon;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChargePacket {
    private final boolean start;
    public ChargePacket(boolean start) { this.start = start; }

    public static void encode(ChargePacket m, FriendlyByteBuf buf) { buf.writeBoolean(m.start); }
    public static ChargePacket decode(FriendlyByteBuf buf) { return new ChargePacket(buf.readBoolean()); }

    public static void handle(ChargePacket m, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer p = c.getSender();
            if (p != null) ChargeLogic.set(p, m.start);
        });
        c.setPacketHandled(true);
    }
}
