package me.epicgodmc.blockstackerx.hook.hologram;

import lombok.Getter;

/**
 * Created by Bert on 14 Oct 2021
 * Copyright © EpicGodMC
 */
public enum HologramProviderType
{

    HD_DISPLAYS(new HolographicDisplays_Hook()),
    CUSTOM(new CustomHolograms_Hook())
    ;

    @Getter
    private HologramHook hook;

    HologramProviderType(HologramHook hook)
    {
        this.hook = hook;
    }
}
