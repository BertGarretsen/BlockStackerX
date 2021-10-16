package me.epicgodmc.blockstackerx.hook.hologram;

import lombok.Getter;
import org.bukkit.Location;

/**
 * Created by Bert on 16 Oct 2021
 * Copyright © EpicGodMC
 */
@Getter
public abstract class StackerHologram
{


    private final String format;

    public StackerHologram(String format) {
        this.format = format;
    }

    public String getFormatReplaced(int amount)
    {
        return getFormat().replace("{value}", String.valueOf(amount));
    }

    public abstract void create(int value, Location location);

    public abstract boolean isSpawned();

    public abstract void update(int newValue);

    public abstract void update(int line, String text);

    public abstract void delete();
}
