package me.epicgodmc.blockstackerx.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.CompMaterial;

/**
 * Created by Bert on 25 Sep 2021
 * Copyright © EpicGodMC
 */
public class InventoryUtils {



    public static boolean take(Player player, ItemStack stack, int amount) {
        if (!containsAtLeast(player, amount, stack))
            return false;

        for (int i = 0; i < amount; i++)
            takeFirstOnePiece(player, stack);

        return true;
    }

    public static boolean takeFirstOnePiece(final Player player, final ItemStack stack) {
        for (final ItemStack item : player.getInventory().getContents())
            if (item.isSimilar(stack)) {
                PlayerUtil.takeOnePiece(player, item);
                return true;
            }
        return false;
    }



    public static boolean containsAtLeast(Player player, int atLeastSize, ItemStack stack) {
        int foundSize = 0;

        for (final ItemStack item : player.getInventory().getContents())
            if (item != null && item.getType() == stack.getType())
                foundSize += item.getAmount();

        return foundSize >= atLeastSize;
    }

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
