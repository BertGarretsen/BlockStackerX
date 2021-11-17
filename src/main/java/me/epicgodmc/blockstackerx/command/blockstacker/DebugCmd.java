package me.epicgodmc.blockstackerx.command.blockstacker;

import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

/**
 * Created by Bert on 27 Aug 2021
 * Copyright © EpicGodMC
 */
public class DebugCmd extends SimpleSubCommand
{


    protected DebugCmd(SimpleCommandGroup parent, String sublabel) {
        super(parent, sublabel);

        setMinArguments(1);
        setUsage("<param> [params]");
    }



    // bs debug {param} {params}

    @Override
    protected void onCommand() {
        if (args[0].equalsIgnoreCase("forceLoadCache"))
        {
            Player target = findPlayer(args[1], "Could not find target");
            IslandCache islandCache = IslandCache.getCache(StackerPlugin.getInstance().getHookManager().getIslandID(target));
            getSender().sendMessage(islandCache.toString());
        }
    }
}
