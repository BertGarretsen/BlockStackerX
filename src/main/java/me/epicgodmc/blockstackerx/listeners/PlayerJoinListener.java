package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.storage.IslandCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Bert on 26 Sep 2021
 * Copyright © EpicGodMC
 */
public class PlayerJoinListener implements Listener
{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        IslandCache.getCache(e.getPlayer());
    }

}
