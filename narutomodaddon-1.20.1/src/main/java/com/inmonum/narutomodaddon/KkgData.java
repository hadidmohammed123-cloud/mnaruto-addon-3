package com.inmonum.narutomodaddon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

/** Kekkei genkai the add-on can hand out. adv = ShinobiCraft advancement path, item = ShinobiCraft item path. */
public class KkgData {
    public record Entry(String adv, String item, boolean nature) {}
    public static final Map<String, Entry> MAP = new LinkedHashMap<>();
    static {
        MAP.put("byakugan", new Entry("byakuganopened", "byakuganhelmet", false));
        MAP.put("sharingan", new Entry("sharinganopened", "sharinganhelmet", false));
        MAP.put("ketsuryugan", new Entry("ketsuryugan_opened", "ketsuryuganhelmet", false));
        MAP.put("shikotsumyaku", new Entry("shikotsumyaku_acquired", "shikotsumyaku", true));
        MAP.put("yooton", new Entry("yooton_acquired", "yooton", true));
        MAP.put("shakuton", new Entry("shakuton_acquired", "shakuton", true));
        MAP.put("hyoton", new Entry("hyoton_acquired", "hyoton", true));
        MAP.put("jiton", new Entry("jiton_acquired", "jiton", true));
        MAP.put("bakuton", new Entry("bakuton_acquired", "bakuton", true));
        MAP.put("ranton", new Entry("ranton_acquired", "ranton", true));
        MAP.put("futton", new Entry("futton_acquired", "futton", true));
        MAP.put("shoton", new Entry("shoton_acquired", "shoton", true));
        MAP.put("mokuton", new Entry("mokuton_acquired", "mokuton", false));
        MAP.put("jinton", new Entry(null, "jinton", true));
    }

    public static boolean isKkgItem(ItemStack s) {
        if (s.isEmpty()) return false;
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(s.getItem());
        if (id == null || !id.getNamespace().equals("narutomod")) return false;
        for (Entry e : MAP.values()) if (e.item() != null && e.item().equals(id.getPath())) return true;
        return false;
    }
}
