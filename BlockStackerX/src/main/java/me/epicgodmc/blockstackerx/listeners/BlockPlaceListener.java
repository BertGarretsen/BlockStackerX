package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.stacker.StackerNbtData;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.util.StackerLocation;
import me.epicgodmc.blockstackerx.util.StackerState;
import me.epicgodmc.blockstackerx.util.StackerUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;

public class BlockPlaceListener implements Listener {

    private final StackerPlugin plugin;

    public BlockPlaceListener(StackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;

        StackerNbtData stackerNbtData = new StackerNbtData(e.getItemInHand());
        if (stackerNbtData.isStacker()) {
            if (!Valid.checkPermission(e.getPlayer(), "blockstackerx.stacker.place")) {
                e.setCancelled(true);
                return;
            }

            Location location = e.getBlock().getLocation();
            Optional<Island> optIslandAt =BentoBox.getInstance().getIslandsManager().getIslandAt(location);
            if (!optIslandAt.isPresent())
            {
                Common.tell(e.getPlayer(), Localization.Stacker_Actions.NO_ISLAND_FOUND_AT_LOCATION);
                e.setCancelled(true);
                return;
            }
            Island island = optIslandAt.get();
            IslandCache islandCache = IslandCache.getCache(island.getUniqueId());

            Player player = e.getPlayer();
            Block block = e.getBlockPlaced();

            StackerSettings settings = StackerRegister.getInstance().getSettings(stackerNbtData.getStackerIdentifier());
            if (settings == null) {
                e.setCancelled(true);
                Common.tell(player, Localization.Stacker_Actions.STACKERTYPE_BROKEN);
                return;
            }

            StackerBlock b;
            if (!stackerNbtData.isUsedStacker()) {
                b = new StackerBlock(player.getUniqueId(), stackerNbtData.getStackerIdentifier(), new StackerLocation(block));
            } else {

                b = new StackerBlock(player.getUniqueId(), stackerNbtData.getStackerIdentifier(), new StackerLocation(block), stackerNbtData.getMaterial(), stackerNbtData.getValue());
            }


            islandCache.addStacker(b);
            Common.tell(e.getPlayer(), Localization.Stacker_Actions.STACKER_PLACED);
        }
    }

//    @EventHandler(priority = EventPriority.LOW)
//    public void onBlockPlace(BlockPlaceEvent e) {
//        if (e.isCancelled()) return;
//
//        StackerNbtData stackerNbtData = new StackerNbtData(e.getItemInHand());
//        if (stackerNbtData.isStacker()) {
//            if (!Valid.checkPermission(e.getPlayer(), "blockstackerx.stacker.place")) {
//                e.setCancelled(true);
//                return;
//            }
//
//
//            if (!plugin.getHookManager().getSkyblockHook().hasIsland(e.getPlayer())) {
//                Common.tell(e.getPlayer(), Localization.Stacker_Actions.NO_ISLAND_FOUND);
//                e.setCancelled(true);
//                return;
//            }
//            IslandCache islandCache = IslandCache.getCache(plugin.getHookManager().getIslandID(e.getPlayer()));
//
//            Player player = e.getPlayer();
//            Block block = e.getBlockPlaced();
//
//            StackerSettings settings = StackerRegister.getInstance().getSettings(stackerNbtData.getStackerIdentifier());
//            if (settings == null) {
//                e.setCancelled(true);
//                Common.tell(player, Localization.Stacker_Actions.STACKERTYPE_BROKEN);
//                return;
//            }
//
//
//            StackerBlock b;
//            if (!stackerNbtData.isUsedStacker()) {
//                b = new StackerBlock(player.getUniqueId(), stackerNbtData.getStackerIdentifier(), new StackerLocation(block));
//            } else {
//
//                b = new StackerBlock(player.getUniqueId(), stackerNbtData.getStackerIdentifier(), new StackerLocation(block), stackerNbtData.getMaterial(), stackerNbtData.getValue());
//            }
//
//
//            islandCache.addStacker(b);
//            Common.tell(e.getPlayer(), Localization.Stacker_Actions.STACKER_PLACED);
//        }
//    }

}
