package com.inmonum.narutomodaddon;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ClientKeys {
    public static final String CATEGORY = "key.categories.narutomodaddon";
    public static final KeyMapping CHARGE = new KeyMapping("key.narutomodaddon.chakra_charge", InputConstants.KEY_V, CATEGORY);
    public static final KeyMapping MENU = new KeyMapping("key.narutomodaddon.menu", InputConstants.KEY_K, CATEGORY);
}
