package me.epicgodmc.blockstackerx;

import lombok.Getter;
import me.epicgodmc.blockstackerx.hook.hologram.HologramHook;
import me.epicgodmc.blockstackerx.hook.hologram.HolographicDisplays_Hook;
import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.hook.skyblock.BentoBox_Hook;
import me.epicgodmc.blockstackerx.hook.skyblock.SkyblockHook;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.managers.AddonsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HookManager
{

    private StackerPlugin plugin;


    @Getter private SkyblockHook skyblockHook;
    @Getter private HologramHook hologramHook;


    public HookManager(StackerPlugin plugin)
    {
        this.plugin = plugin;
        if (Common.doesPluginExist("BentoBox"))
        {
            AddonsManager addonsManager = BentoBox.getInstance().getAddonsManager();
            if (addonsManager.getAddonByName("BSkyBlock").isPresent()
                && addonsManager.getAddonByName("Level").isPresent())
            {
                this.skyblockHook = new BentoBox_Hook();
                Common.log("Using BentoBox + (BSkyBlock & Level Addon) as skyblock dependency!");
            }else Common.log("Found BentoBox, but could not locate required addons: (BSkyBlock & Level Addon)");
        }

        if (Common.doesPluginExist("HolographicDisplays"))
        {
            this.hologramHook = new HolographicDisplays_Hook();
            Common.log("Using HolographicDisplays as Hologram dependency!");
        }
    }

    protected boolean verify()
    {
        return skyblockHook != null &&
                hologramHook != null;
    }

    protected List<String> getMissingDependencies()
    {
        List<String> output = new ArrayList<>();

        if (skyblockHook == null) output.add("Skyblock");
        if (hologramHook == null) output.add("Hologram");


        return output;
    }

    public StackerHologram getNewHologram(String format) {
        return new StackerHologram(hologramHook, format);
    }

    public String getIslandID(Player player)
    {
        return this.skyblockHook.getIslandId(player);
    }

    public boolean canModifyStacker(Player player, StackerBlock stacker)
    {
        return this.skyblockHook.canModifyStacker(stacker, player);
    }
}
