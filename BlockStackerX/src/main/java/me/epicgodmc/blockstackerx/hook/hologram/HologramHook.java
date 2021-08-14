package me.epicgodmc.blockstackerx.hook.hologram;

import org.bukkit.Location;

import java.util.List;

public interface HologramHook
{

    Object createHologram(Location location, List<String> lines);

    void deleteHologram(Object hologram);

    Object updateLine(Object hologram, int line, String text);

}
