package me.epicgodmc.blockstackerx.hook.skyblock;

import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.UUID;

public interface SkyblockHook extends Listener
{

    Collection<UUID> getTeam(World world, UUID uuid);

    boolean hasSameTeam(UUID player1, UUID player2);

    boolean canModifyStacker(StackerBlock stackerBlock, Player player);

    boolean isLastOnline(Player player);

    boolean hasIsland(OfflinePlayer player);

    String getIslandId(OfflinePlayer player);

    String getName();

}
