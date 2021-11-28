package me.epicgodmc.blockstackerx.listeners;

import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.menu.StackerMenu;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.Settings;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.stacker.StackerPermission;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.util.StackerUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;


/**
 * Created by Bert on 28 Aug 2021
 * Copyright © EpicGodMC
 */
public class BlockInteractListener implements Listener {

    private final StackerPlugin plugin;

    public BlockInteractListener(StackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        if (!e.hasBlock()) return;
        if (!Remain.isInteractEventPrimaryHand(e)) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        Optional<Island> optionalIsland = BentoBox.getInstance().getIslandsManager().getIslandAt(block.getLocation());
        if (optionalIsland.isPresent()) {
            Island island = optionalIsland.get();
            IslandCache cache = IslandCache.getCache(island.getUniqueId());

            if (cache.hasStackerAt(block.getLocation())) {
                e.setCancelled(true);
                Action action = e.getAction();
                StackerBlock stackerBlock = cache.getStackerAt(block.getLocation());

                if (stackerBlock == null) {
                    Common.tell(e.getPlayer(), Localization.Stacker_Actions.ERROR);
                    return;
                }

                if (action == Settings.STACKER_ADD_ACTION) {
                    if (e.getItem() == null || !e.getItem().getType().isBlock()) {
                        if (!stackerBlock.hasPermission(e.getPlayer(), StackerPermission.OPEN_STACKER)) {
                            Common.tell(e.getPlayer(), Localization.Stacker_Actions.STACKER_NO_PERMISSION);
                            return;
                        }
                        StackerMenu menu = new StackerMenu(e.getPlayer().getUniqueId(), stackerBlock);
                        menu.displayTo(e.getPlayer());
                        return;
                    } else handleAdd(e, stackerBlock, e.getItem());
                }
                if (action == Settings.STACKER_TAKE_ACTION) {
                    handleSub(e, stackerBlock);
                }
            }
        }
    }

    /**
     * Handles the adding of blocks to the stacker
     *
     * @param e            The Event
     * @param stackerBlock The StackerBlock
     * @param hand         The item in player's hand
     */
    private void handleAdd(PlayerInteractEvent e, StackerBlock stackerBlock, ItemStack hand) {

        Player player = e.getPlayer();

        if (!Valid.checkPermission(player, "blockstackerx.stacker.add")) {
            return;
        }

        StackerSettings stackerSettings = StackerRegister.getInstance().getSettings(stackerBlock.getIdentifier());

        if (stackerSettings == null) {
            Common.tell(player, Localization.Stacker_Actions.STACKERTYPE_BROKEN);
            return;
        }

        CompMaterial material = CompMaterial.fromMaterial(hand.getType());

        if (!StackerUtils.isCleanBlock(hand)) {
            Common.tell(player, Localization.Stacker_Actions.MATERIAL_CONTAINS_NBT);
            return;
        }

        if (!stackerSettings.getAvailableBlocks().contains(material)) {
            Common.tell(player, Localization.Stacker_Actions.INVALID_BLOCK.replace("{block}", ItemUtil.bountifyCapitalized(material)).replace("{blocks_available}", Common.join(stackerSettings.getAvailableBlocks(), ", ")));
            return;
        }

        if (!plugin.getHookManager().getSkyblockHook().canModifyStacker(stackerBlock, player)) {
            OfflinePlayer op = Remain.getOfflinePlayerByUUID(stackerBlock.getOwner());
            if (op != null) {
                Common.tell(player, Localization.Stacker_Actions.MODIFY_DENY.replace("{owner}", op.getName()));
                return;
            } else {
                Common.tell(player, Localization.Stacker_Actions.ERROR);
            }
        }

        if (!stackerBlock.hasPermission(player.getUniqueId(), StackerPermission.ADD_BLOCKS)) {
            Common.tell(player, Localization.Stacker_Actions.STACKER_NO_PERMISSION);
            return;
        }


        if (stackerBlock.getValue() == 0) {
            stackerBlock.setStackMaterial(material);
            Common.tell(player, Localization.Stacker_Actions.MATERIAL_CHOSEN.replace("{block}", ItemUtil.bountifyCapitalized(material)));
        }
        if (stackerBlock.getStackMaterial().is(material.getMaterial())) {
            if (!player.isSneaking()) {
                if (stackerBlock.canAddValue(1)) {
                    StackerUtils.takeFirstOne(e.getPlayer(), hand);
                    stackerBlock.incrementValue(1);
                    tellAdded(player, 1, material);
                } else {
                    Common.tell(player, Localization.Stacker_Actions.MAX_STORAGE_REACHED.replace("{amount}", Integer.toString(stackerSettings.getMaxStorage())));
                }
            } else {
                if (stackerBlock.canAddValue(hand.getAmount())) {
                    int handAmt = hand.getAmount();
                    boolean success = StackerUtils.takeClean(player, material.toItem(), handAmt);
                    if (success) {
                        stackerBlock.incrementValue(handAmt);
                        tellAdded(player, handAmt, material);
                    } else {
                        Common.tell(player, Localization.Stacker_Actions.ERROR);
                    }
                } else if (stackerBlock.getSpaceLeft() >= 1) {
                    int storageLeft = stackerBlock.getSpaceLeft();
                    boolean success = StackerUtils.takeClean(player, material.toItem(), storageLeft);
                    if (success) {
                        stackerBlock.incrementValue(storageLeft);
                        tellAdded(player, storageLeft, material);
                    }
                } else {
                    Common.tell(player, Localization.Stacker_Actions.MAX_STORAGE_REACHED.replace("{amount}", String.valueOf(stackerSettings.getMaxStorage())));
                }
            }
        }
    }

    /**
     * Handles subtraction of blocks from stackers
     *
     * @param e            The Event
     * @param stackerBlock The Stacker Block
     */
    private void handleSub(PlayerInteractEvent e, StackerBlock stackerBlock) {
        Player player = e.getPlayer();

        if (!Valid.checkPermission(player, "blockstackerx.stacker.subtract")) {
            return;
        }

        if (!plugin.getHookManager().getSkyblockHook().canModifyStacker(stackerBlock, player)) {
            OfflinePlayer op = Remain.getOfflinePlayerByUUID(stackerBlock.getOwner());
            if (op != null) {
                Common.tell(player, Localization.Stacker_Actions.MODIFY_DENY.replace("{owner}", op.getName()));
                return;
            } else {
                Common.tell(player, Localization.Stacker_Actions.ERROR);
            }
            return;
        }

        if (!stackerBlock.hasPermission(player.getUniqueId(), StackerPermission.REMOVE_BLOCKS)) {
            Common.tell(player, Localization.Stacker_Actions.STACKER_NO_PERMISSION);
            return;
        }

        if (!stackerBlock.hasStackMaterial()) {
            Common.tell(player, Localization.Stacker_Actions.MIN_STORAGE_REACHED);
            return;
        }

        Material stackMaterial = stackerBlock.getStackMaterial().getMaterial();

        if (!stackerBlock.canSubtractValue(1)) {
            Common.tell(player, Localization.Stacker_Actions.MIN_STORAGE_REACHED);
            return;
        }


        if (!player.isSneaking()) {
            //subtract 1
            StackerUtils.giveOrDropItems(player, stackerBlock.getContainingItemStack(1));
            tellSubtracted(player, 1, stackMaterial);
            stackerBlock.subtractValue(1);


        } else {
            //subtract 64
            if (stackerBlock.canSubtractValue(64)) {
                StackerUtils.giveOrDropItems(player, stackerBlock.getContainingItemStack(64));
                tellSubtracted(player, 64, stackMaterial);
                stackerBlock.subtractValue(64);

            } else {
                int value = stackerBlock.getValue();
                StackerUtils.giveOrDropItems(player, stackerBlock.getContainingItemStack(value));
                tellSubtracted(player, value, stackMaterial);
                stackerBlock.setValue(0);

            }

        }
    }


    private void tellAdded(Player player, int amount, CompMaterial block) {
        tellAdded(player, amount, block.getMaterial());
    }

    private void tellAdded(Player player, int amount, Material block) {
        Common.tell(player, Localization.Stacker_Actions.BLOCK_ADDED.replace("{amount}", Integer.toString(amount)).replace("{block}", ItemUtil.bountifyCapitalized(block)));
    }

    private void tellSubtracted(Player player, int amount, Material material) {
        Common.tell(player, Localization.Stacker_Actions.BLOCK_SUBTRACTED.replace("{amount}", Integer.toString(amount)).replace("{block}", ItemUtil.bountifyCapitalized(material)));

    }

}
