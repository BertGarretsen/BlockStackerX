package me.epicgodmc.blockstackerx.hook.hologram.impl;

import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.util.SimpleTextHologram;
import org.bukkit.Location;
import org.mineacademy.fo.Common;

/**
 * Created by Bert on 16 Oct 2021
 * Copyright © EpicGodMC
 */
public class CustomStackerHologram extends StackerHologram
{

    private SimpleTextHologram hologram;

    public CustomStackerHologram(String format) {
        super(format);
    }

    @Override
    public void create(int value, Location location) {
        hologram = new SimpleTextHologram(location);
        update(value);
        hologram.spawn();
    }

    @Override
    public boolean isSpawned() {
        return hologram != null && hologram.isSpawned();
    }

    @Override
    public void update(int newValue) {
        update(0, getFormatReplaced(newValue));
    }

    @Override
    public void update(int line, String text) {
        if (isSpawned())
        {
            hologram.removeLore();
            hologram.getLoreLines().add(line, Common.colorize(text));
            hologram.updateLore();
        }
    }

    @Override
    public void delete() {
        hologram.remove();
    }

}
