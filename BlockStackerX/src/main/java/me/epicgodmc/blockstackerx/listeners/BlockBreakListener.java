package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.util.StackerUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;


public class BlockBreakListener implements Listener {

    private final StackerPlugin plugin;

    public BlockBreakListener(StackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location l = e.getBlock().getLocation();
        Valid.checkPermission(e.getPlayer(), "blockstackerx.stacker.break");

        if (plugin.getHookManager().getSkyblockHook().hasIsland(e.getPlayer())) {
            IslandCache cache = IslandCache.getCache(plugin.getHookManager().getIslandID(e.getPlayer()));
            if (!cache.hasStackerAt(l) || plugin.getHookManager().canModifyStacker(e.getPlayer(), cache.getStackerAt(l))) {
                Common.tell(e.getPlayer(), Localization.Stacker_Actions.NO_PERMISSION);
                return;
            }
            if (StackerUtils.isPickaxe(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                e.setCancelled(true);
                //TODO give stacker

            }
        }
    }
}
