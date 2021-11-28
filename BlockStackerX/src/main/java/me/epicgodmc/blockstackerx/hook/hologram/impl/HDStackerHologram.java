package me.epicgodmc.blockstackerx.hook.hologram.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import org.bukkit.Location;
import org.mineacademy.fo.Common;

/**
 * Created by Bert on 16 Oct 2021
 * Copyright © EpicGodMC
 */
public class HDStackerHologram extends StackerHologram {

    private Hologram hologram;

    public HDStackerHologram(String format) {
        super(format);
    }

    @Override
    public void create(int value, Location location) {
        this.hologram = HologramsAPI.createHologram(StackerPlugin.getInstance(), location);
        this.update(value);
    }

    @Override
    public boolean isSpawned() {
        return hologram != null && !hologram.isDeleted();
    }

    @Override
    public void update(int newValue) {
        this.update(0, getFormatReplaced(newValue));
    }

    @Override
    public void update(int line, String text) {
        if (isSpawned()) {
            hologram.clearLines();
            hologram.insertTextLine(line, Common.colorize(text));
        }
    }


    @Override
    public void delete() {
        hologram.delete();
    }
}
