package me.epicgodmc.blockstackerx.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.remain.CompMaterial;

/**
 * Created by Bert on 25 Sep 2021
 * Copyright © EpicGodMC
 */
public class InventoryUtils {

    public static int countSimilar(Inventory inventory, ItemStack itemStack) {
        int ret = 0;
        ItemStack[] var3 = inventory.getContents();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            ItemStack item = var3[var5];
            if (item != null && item.isSimilar(itemStack)) {
                ret += item.getAmount();
            }
        }

        return ret;
    }

    public static int roomLeft(PlayerInventory inv, CompMaterial compMaterial) {
        return roomLeft(inv, compMaterial.getMaterial());
    }

    public static int roomLeft(PlayerInventory inv, Material m) {
        int count = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) {
                count += m.getMaxStackSize();
            }
            if (is != null) {
                if (is.getType() == m) {
                    count += (m.getMaxStackSize() - is.getAmount());
                }
            }
        }
        return count;
    }


}
