package me.epicgodmc.blockstackerx.hook.skyblock;

import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.Remain;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.events.BentoBoxEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.*;

public final class BentoBox_Hook implements SkyblockHook, Listener {


    private String addonName;
    private UUID cachedWorld;

    public BentoBox_Hook(String addon) {
        this.addonName = addon;
        StackerPlugin.getInstance().registerListener(this);
    }


    @EventHandler
    public void onLevelCalculate(BentoBoxEvent e) {
        Map<String, Object> keyValues = e.getKeyValues();
        if ("IslandLevelCalculatedEvent".equals(e.getEventName()))
        {
            Island island = (Island) keyValues.get("island");
            long level = (long) keyValues.get("level");
            IslandCache islandCache = IslandCache.getCache(island.getUniqueId());

            Collection<StackerBlock> stackers = islandCache.getStackers().values();

            for (StackerBlock stacker : stackers) {
                if (stacker.hasStackMaterial())
                {
                    double stackerLevels = stacker.calculateLevels();
                    level += stackerLevels;
                }
            }
            keyValues.put("level", level);
            e.setKeyValues(keyValues);
        }
    }


    @Override
    public Collection<UUID> getTeam(World world, UUID uuid) {
        Island island = BentoBox.getInstance().getIslands().getIsland(world, uuid);
        if (island != null) {
            return island.getMemberSet();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasSameTeam(UUID player1, UUID player2) {
        return getTeam(getWorld(), player1).contains(player2);
    }

    @Override
    public boolean canModifyStacker(StackerBlock stackerBlock, Player player) {
        return stackerBlock.getOwner().equals(player.getUniqueId())
                || StackerRegister.getInstance().getSettings(stackerBlock.getIdentifier()).isTeamStacking()
                && this.hasSameTeam(stackerBlock.getOwner(), player.getUniqueId())
                || PlayerUtil.hasPerm(player, "blockstackerx.stacker.bypass");
    }

    @Override
    public boolean isLastOnline(Player player) {
        Collection<UUID> team = getTeam(getWorld(), player.getUniqueId());
        int onlineCount = 0;

        for (UUID uuid : team) {
            if (Remain.getOfflinePlayerByUUID(uuid).isOnline()) onlineCount++;
        }
        return onlineCount == 1;
    }

    @Override
    public boolean hasIsland(OfflinePlayer player) {
        return !getIslandId(player).equalsIgnoreCase("UNKNOWN");
    }

    @Override
    public String getIslandId(OfflinePlayer player) {
        Island island = BentoBox.getInstance().getIslands().getIsland(getWorld(), player.getUniqueId());
        if (island == null) return "UNKNOWN";
        return island.getUniqueId();
    }

    public World getWorld()
    {
        if (cachedWorld == null)
        {
            Optional<World> world = Bukkit.getWorlds().stream().filter(this::isCorrectWorld).findFirst();
            return world.orElse(null);
        }else{
            return Bukkit.getWorld(cachedWorld);
        }
    }

    public boolean isCorrectWorld(World world)
    {
        Optional<Addon> addon = BentoBox.getInstance().getAddonsManager().getAddonByName(addonName);
        return addon.filter(value -> ((GameModeAddon) value).inWorld(world)).isPresent();
    }

    @Override
    public String getName() {
        return "BentoBox";
    }

}
