package me.epicgodmc.blockstackerx.hook.hologram;

import me.epicgodmc.blockstackerx.util.SimpleTextHologram;
import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.SimpleHologram;

import java.util.List;

/**
 * Created by Bert on 14 Oct 2021
 * Copyright © EpicGodMC
 */
public class CustomHolograms_Hook implements HologramHook {


    @Override
    public Object createHologram(Location location, List<String> lines) {
        SimpleHologram hologram = new SimpleTextHologram(location);
        hologram.setLore(Common.toArray(lines));
        hologram.spawn();

        return hologram;
    }

    @Override
    public void deleteHologram(Object hologram) {
        Valid.checkBoolean(hologram instanceof SimpleHologram, "Passed object is not of 'me.epicgodmc.blockstackerx.lib.fo.model.SimpleHologram'");

        SimpleHologram hg = (SimpleHologram) hologram;
        hg.remove();
    }

    @Override
    public Object updateLine(Object hologram, int line, String text) {
        Valid.checkNotNull(hologram, "Hologram is null");
        Valid.checkBoolean(line >= 0, "Line is of invalid number: " + line);
        Valid.checkBoolean(hologram instanceof SimpleTextHologram, "Passed object is not of 'me.epicgodmc.blockstackerx.lib.fo.model.SimpleHologram'");

        SimpleTextHologram hg = (SimpleTextHologram) hologram;
        hg.getLoreLines().remove(line);
        hg.getLoreLines().add(line, Common.colorize(text));
        hg.updateLore();
        return hg;
    }

    @Override
    public void disable() {
        SimpleHologram.deleteAll();
    }
}
