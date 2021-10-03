package me.epicgodmc.blockstackerx.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.EntityUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.nbt.NBTItem;

import java.util.Map;

public class StackerUtils {

    public static boolean isCleanBlock(ItemStack hand) {
        NBTItem nbtItem = new NBTItem(hand);
        return !nbtItem.hasNBTData() || nbtItem.getKeys().isEmpty();
    }


    public static void giveOrDropItems(Player player, ItemStack... itemStack) {
        Map<Integer, ItemStack> excess = PlayerUtil.addItems(player.getInventory(), itemStack);
        for (Map.Entry<Integer, ItemStack> entry : excess.entrySet()) {
            EntityUtil.dropItem(player.getLocation(), entry.getValue(), item -> {
            });
        }
    }


}
