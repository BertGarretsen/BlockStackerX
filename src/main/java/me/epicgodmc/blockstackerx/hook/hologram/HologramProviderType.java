package me.epicgodmc.blockstackerx.hook.hologram;

import lombok.Getter;
import me.epicgodmc.blockstackerx.hook.hologram.impl.BSXStackerHologram;
import me.epicgodmc.blockstackerx.hook.hologram.impl.HDStackerHologram;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;

/**
 * Created by Bert on 14 Oct 2021
 * Copyright © EpicGodMC
 */
public enum HologramProviderType {

    BSX_DISPLAYS(BSXStackerHologram::new,"Using the BlockStackerX implementation for holograms."),

    HD_DISPLAYS((format, block) -> new HDStackerHologram(format),"Using Holographic-Displays as hologram dependency."),
    ;


    @Getter private final HologramProvider provider;
    @Getter private final String enableMessage;

    HologramProviderType(HologramProvider hologram, String enableMessage) {
        this.enableMessage = enableMessage;
        this.provider = hologram;
    }

    public interface HologramProvider {
        StackerHologram get(String format, StackerBlock block);
    }
}
