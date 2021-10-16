package me.epicgodmc.blockstackerx.hook.hologram;

import lombok.Getter;
import me.epicgodmc.blockstackerx.hook.hologram.impl.HDStackerHologram;

/**
 * Created by Bert on 14 Oct 2021
 * Copyright © EpicGodMC
 */
public enum HologramProviderType {

    HD_DISPLAYS("Using Holographic-Displays as hologram dependency"),
    CUSTOM("Using custom holograms provided by BlockStackerX");

    @Getter
    String enableMessage;

    HologramProviderType(String enableMessage) {
        this.enableMessage = enableMessage;
    }



    public StackerHologram getHologram(String format)
    {
        if (this == HD_DISPLAYS)
        {
            return new HDStackerHologram(format);
        }
        if (this == CUSTOM)
        {
            //TODO
        }
        return null;
    }

}
