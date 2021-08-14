package me.epicgodmc.blockstackerx.hook.skyblock;

import me.epicgodmc.blockstackerx.StackerBlock;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.UUID;

public interface SkyblockHook extends Listener
{

    Collection<UUID> getTeam(World world, UUID uuid);

    boolean hasSameTeam(World world, UUID player1, UUID player2);

    boolean canModifyStacker(StackerBlock stackerBlock, Player player);

    boolean isLastOnline(Player player);

    boolean hasIsland(Player player);

    String getIslandId(Player player);

    String getName();

}
