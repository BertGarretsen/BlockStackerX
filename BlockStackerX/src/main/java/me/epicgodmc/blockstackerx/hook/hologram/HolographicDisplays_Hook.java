package me.epicgodmc.blockstackerx.hook.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.epicgodmc.blockstackerx.StackerPlugin;
import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

import java.util.List;

public class HolographicDisplays_Hook implements HologramHook
{

    @Override
    public Object createHologram(Location location, List<String> lines) {
        Hologram hologram = HologramsAPI.createHologram(StackerPlugin.getInstance(), location);

        lines.forEach((l) -> hologram.appendTextLine(Common.colorize(l)));
        return hologram;
    }

    @Override
    public void deleteHologram(Object hologram) {
        Valid.checkBoolean(hologram instanceof Hologram, "Passed object is not of 'com.gmail.filoghost.holographicdisplays.api.Hologram'");

        Hologram hg = (Hologram) hologram;
        hg.delete();
    }

    @Override
    public Object updateLine(Object hologram, int line, String text) {
        Valid.checkNotNull(hologram, "Hologram is null");
        Valid.checkBoolean(line >= 0, "Line is of invalid number: "+line);
        Valid.checkBoolean(hologram instanceof Hologram, "Passed object is not of 'com.gmail.filoghost.holographicdisplays.api.Hologram'");

        Hologram hg = (Hologram) hologram;
        hg.getLine(line).removeLine();
        hg.insertTextLine(line, Common.colorize(text));

        return hg;
    }

}
