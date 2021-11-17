package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.Global;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.util.CustomBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Created by Bert on 01 Oct 2021
 * Copyright © EpicGodMC
 */
public class BlockModifyListener implements Listener {


    @EventHandler
    public void blockExplode(BlockExplodeEvent e) {
        for (Block block : e.blockList()) {
            if (isStacker(block)){
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            if (isStacker(block)){
                e.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent e) {
        for (Block block : e.getBlocks()) {
            if (isStacker(block)){
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void pisonRetract(BlockPistonRetractEvent e) {
        for (Block block : e.getBlocks()) {
            if (isStacker(block)){
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void entityChange(EntityChangeBlockEvent e) {
        if (isStacker(e.getBlock())) e.setCancelled(true);
    }

    public boolean isStacker(Location location)
    {
        Block block = location.getBlock();
        return isStacker(block);
    }


    public boolean isStacker(Block block)
    {
        final PersistentDataContainer container = new CustomBlockData(block, StackerPlugin.getInstance());
        return container.has(Global.STACKER_KEY, PersistentDataType.BYTE);
    }



}
