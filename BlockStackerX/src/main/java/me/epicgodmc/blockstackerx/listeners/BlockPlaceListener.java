package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.StackerBlock;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.util.SimpleLocation;
import me.epicgodmc.blockstackerx.util.StackerState;
import me.epicgodmc.blockstackerx.util.StackerUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

public class BlockPlaceListener implements Listener {

    private final StackerPlugin plugin;

    public BlockPlaceListener(StackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        Valid.checkPermission(e.getPlayer(), "blockstackerx.stacker.place");
        if (StackerUtils.isStacker(e.getItemInHand())) {
            if (!plugin.getHookManager().getSkyblockHook().hasIsland(e.getPlayer())) {
                Common.tell(e.getPlayer(), Localization.Stacker_Actions.NO_ISLAND_FOUND);
                e.setCancelled(true);
                return;
            }
            IslandCache islandCache = IslandCache.getCache(plugin.getHookManager().getIslandID(e.getPlayer()));

            Player player = e.getPlayer();
            Block block = e.getBlockPlaced();
            StackerState state = new StackerState(e.getItemInHand());

            StackerSettings settings = StackerRegister.getInstance().getSettings(state.getId());
            Valid.checkNotNull(settings, "XStacker object is null");

            StackerBlock b = new StackerBlock(player.getUniqueId(), state.getId(), new SimpleLocation(block.getLocation()));
            islandCache.addStacker(b);
            Common.tell(e.getPlayer(), Localization.Stacker_Actions.STACKER_PLACED);
        }
    }
}
