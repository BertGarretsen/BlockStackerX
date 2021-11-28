package me.epicgodmc.blockstackerx.stacker;

import lombok.Getter;
import me.epicgodmc.blockstackerx.util.StackerState;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBTCompound;
import org.mineacademy.fo.remain.nbt.NBTItem;

/**
 * Created by Bert on 02 Oct 2021
 * Copyright © EpicGodMC
 */
public class StackerNbtData {

    @Getter
    private String stackerIdentifier;
    @Getter
    private StackerState stackerState;
    @Getter
    private Integer value;
    @Getter
    private CompMaterial material;

    public StackerNbtData(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound compound = nbtItem.getCompound("stacker_data");
        if (compound != null) {
            stackerIdentifier = compound.getString("StackerIdentifier");
            StackerState.getByTag(compound.getString("StackerState")).ifPresent(state -> this.stackerState = state);
            if (compound.hasKey("Value"))
            {
                this.value = compound.getInteger("Value");
            }
            if (compound.hasKey("Material"))
            {
                String material = compound.getString("Material");
                if (!material.equals("NA"))
                {
                    this.material = CompMaterial.fromStringStrict(material);
                }
            }
        }
    }


    public boolean isStacker()
    {
        return stackerIdentifier != null && stackerState != null;
    }

    public boolean hasStackMaterial()
    {
        return material != null;
    }

    public boolean isUsedStacker()
    {
        return isStacker() && value != null;
    }
}
