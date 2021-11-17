package me.epicgodmc.blockstackerx.menu.common;

import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.button.Button;

/**
 * Created by Bert on 17 Sep 2021
 * Copyright © EpicGodMC
 */
public abstract class DefinedButton extends Button
{

    private final ItemStack itemStack;

    public DefinedButton(ItemStack item)
    {
        this.itemStack = item;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }
}
