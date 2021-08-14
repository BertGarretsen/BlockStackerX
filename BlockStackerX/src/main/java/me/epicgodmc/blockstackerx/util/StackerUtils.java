package me.epicgodmc.blockstackerx.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBTItem;

public class StackerUtils
{

    public static boolean isCleanBlock(ItemStack hand)
    {
        NBTItem nbtItem = new NBTItem(hand);
        return !nbtItem.hasNBTData() || nbtItem.getKeys().isEmpty();
    }


    public static boolean isStacker(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getCompound("stacker_data") != null;
    }

    public static boolean isPickaxe(Material mat)
    {
        return CompMaterial.WOODEN_PICKAXE.is(mat) ||
                CompMaterial.GOLDEN_PICKAXE.is(mat) ||
                CompMaterial.STONE_PICKAXE.is(mat) ||
                CompMaterial.IRON_PICKAXE.is(mat) ||
                CompMaterial.DIAMOND_PICKAXE.is(mat) ||
                CompMaterial.NETHERITE_PICKAXE.is(mat);
    }

}
