package me.epicgodmc.blockstackerx.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.EntityUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.nbt.NBTItem;

import java.util.Map;

public class StackerUtils {

    public static boolean takeClean(Player player, ItemStack stack, int amount) {
        if (!containsAtLeast(player, amount, stack))
            return false;

        for (int i = 0; i < amount; i++)
            takeFirstOne(player, stack);

        return true;
    }

    public static boolean takeFirstOne(final Player player, final ItemStack stack) {
        for (final ItemStack item : player.getInventory().getContents())
            if (item != null && item.isSimilar(stack) && StackerUtils.isCleanBlock(item)) {
                PlayerUtil.takeOnePiece(player, item);
                return true;
            }
        return false;
    }

    public static boolean containsAtLeast(Player player, int atLeastSize, ItemStack stack) {
        int foundSize = 0;

        for (final ItemStack item : player.getInventory().getContents())
            if (item != null && item.getType() == stack.getType() && StackerUtils.isCleanBlock(item))
                foundSize += item.getAmount();

        return foundSize >= atLeastSize;
    }

    public static int countSimilar(Inventory inventory, ItemStack itemStack) {
        int ret = 0;
        ItemStack[] var3 = inventory.getContents();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            ItemStack item = var3[var5];
            if (item != null && item.isSimilar(itemStack) && StackerUtils.isCleanBlock(item)) {
                ret += item.getAmount();
            }
        }

        return ret;
    }


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
