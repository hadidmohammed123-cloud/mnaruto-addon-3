package com.inmonum.narutomodaddon;

import net.minecraftforge.common.ForgeConfigSpec;

public class AddonConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue NINJA_START;
    public static final ForgeConfigSpec.BooleanValue REROLL_KKG;
    public static final ForgeConfigSpec.BooleanValue KEEP_CHAKRA;
    public static final ForgeConfigSpec.DoubleValue NINJA_START_XP;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        NINJA_START = b.comment("New players automatically become ninjas when they join.").define("ninjaStart", true);
        NINJA_START_XP = b.comment("Ninja XP given when a player is started as a ninja.").defineInRange("ninjaStartXp", 10.0D, 0.0D, 100000.0D);
        REROLL_KKG = b.comment("Let every player use /changekkg on themselves (otherwise only ops).").define("rerollKkg", true);
        KEEP_CHAKRA = b.comment("Keep ninja XP / chakra on death (sets ShinobiCraft's keepNinjaXp game rule).").define("keepChakra", false);
        SPEC = b.build();
    }
}
