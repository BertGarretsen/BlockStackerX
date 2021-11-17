package me.epicgodmc.blockstackerx.util;

import lombok.Getter;
import org.mineacademy.fo.collection.SerializedMap;

/**
 * Created by Bert on 17 Sep 2021
 * Copyright © EpicGodMC
 */
@Getter
public class InventoryItem extends ConfigItem
{

    private int slot;
    private boolean enabled;

    public InventoryItem(SerializedMap map) {
        super(map);
        this.slot = map.getInteger("Slot", 0);
        this.enabled = map.getBoolean("Enabled", true);
    }
}
