package me.epicgodmc.blockstackerx.hook.skyblock;

import me.epicgodmc.blockstackerx.StackerBlock;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.Remain;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public final class BentoBox_Hook implements SkyblockHook {


    @Override
    public Collection<UUID> getTeam(World world, UUID uuid) {
        Island island = BentoBox.getInstance().getIslands().getIsland(world, uuid);
        if (island != null) {
            return island.getMemberSet();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasSameTeam(World world, UUID player1, UUID player2) {
        return getTeam(world, player1).contains(player2);
    }

    @Override
    public boolean canModifyStacker(StackerBlock stackerBlock, Player player) {
        return stackerBlock.getOwner().equals(player.getUniqueId())
                || StackerRegister.getInstance().getSettings(stackerBlock.getIdentifier()).isTeamStacking()
                && this.hasSameTeam(stackerBlock.getSimpleLocation().getLocation().getWorld(), stackerBlock.getOwner(), player.getUniqueId())
                || PlayerUtil.hasPerm(player, "blockstackerx.stacker.bypass");
    }

    @Override
    public boolean isLastOnline(Player player) {
        Collection<UUID> team = getTeam(player.getWorld(), player.getUniqueId());
        int onlineCount = 0;

        for (UUID uuid : team) {
            if (Remain.getOfflinePlayerByUUID(uuid).isOnline()) onlineCount++;
        }
        return onlineCount == 1;
    }

    @Override
    public boolean hasIsland(Player player) {
        return !getIslandId(player).equalsIgnoreCase("UNKNOWN");
    }

    @Override
    public String getIslandId(Player player) {
        Island island = BentoBox.getInstance().getIslands().getIsland(player.getWorld(), player.getUniqueId());
        if (island == null) return "UNKNOWN";
        return island.getUniqueId();
    }

    @Override
    public String getName() {
        return "BentoBox";
    }

}
