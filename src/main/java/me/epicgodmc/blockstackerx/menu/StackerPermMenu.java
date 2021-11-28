package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.stacker.StackerPermission;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bert on 30 Sep 2021
 * Copyright © EpicGodMC
 */
public class StackerPermMenu extends Menu {


    private final Button addBlocksPermButton;
    private final Button removeBlocksPermButton;
    private final Button openStackerPermButton;
    private final Button pickupStackerPermButton;
    private final Button editPermsPermButton;


    public StackerPermMenu(Menu parent, StackerBlock block) {
        super(parent);
        setSize(3 * 9);

        List<StackerPermission> perms = block.getActivePerms();


        addBlocksPermButton = new Button() {

            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                boolean bool = perms.contains(StackerPermission.ADD_BLOCKS);
                if (bool) block.removePermission(StackerPermission.ADD_BLOCKS);
                else block.addPermission(StackerPermission.ADD_BLOCKS);
                redraw();
            }

            @Override
            public ItemStack getItem() {
                boolean bool = perms.contains(StackerPermission.ADD_BLOCKS);
                return ItemCreator.of(getMaterial(bool))
                        .name("&a&lAdd Blocks")
                        .lores(Arrays.asList(
                                "",
                                "&2&lDescription",
                                "&aAllows your island to",
                                "&2Add &ablocks to this stacker",
                                "",
                                "&7(( Allowed: &a" + bool + "&7 ))"
                        ))
                        .build().make();
            }
        };

        removeBlocksPermButton = new Button() {

            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                boolean bool = perms.contains(StackerPermission.REMOVE_BLOCKS);
                if (bool) block.removePermission(StackerPermission.REMOVE_BLOCKS);
                else block.addPermission(StackerPermission.REMOVE_BLOCKS);
                redraw();
            }

            @Override
            public ItemStack getItem() {
                boolean bool = perms.contains(StackerPermission.REMOVE_BLOCKS);
                return ItemCreator.of(getMaterial(bool))
                        .name("&a&lRemove Blocks")
                        .lores(Arrays.asList(
                                "",
                                "&2&lDescription",
                                "&aAllows your island to",
                                "&2Remove &ablocks from this stacker",
                                "",
                                "&7(( Allowed: &a" + bool + "&7 ))"
                        ))
                        .build().make();
            }
        };

        openStackerPermButton = new Button() {

            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                boolean bool = perms.contains(StackerPermission.OPEN_STACKER);
                if (bool) block.removePermission(StackerPermission.OPEN_STACKER);
                else block.addPermission(StackerPermission.OPEN_STACKER);
                redraw();
            }

            @Override
            public ItemStack getItem() {
                boolean bool = perms.contains(StackerPermission.OPEN_STACKER);
                return ItemCreator.of(getMaterial(bool))
                        .name("&a&lOpen Stacker")
                        .lores(Arrays.asList(
                                "",
                                "&2&lDescription",
                                "&aAllows your island to",
                                "&2Open &athe menu of this stacker",
                                "",
                                "&7(( Allowed: &a" + bool + "&7 ))"
                        ))
                        .build().make();
            }
        };

        pickupStackerPermButton = new Button() {

            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                boolean bool = perms.contains(StackerPermission.PICKUP_STACKER);
                if (bool) block.removePermission(StackerPermission.PICKUP_STACKER);
                else block.addPermission(StackerPermission.PICKUP_STACKER);
                redraw();
            }

            @Override
            public ItemStack getItem() {
                boolean bool = perms.contains(StackerPermission.PICKUP_STACKER);
                return ItemCreator.of(getMaterial(bool))
                        .name("&a&lPickup Stacker")
                        .lores(Arrays.asList(
                                "",
                                "&2&lDescription",
                                "&aAllows your island to",
                                "&2Pickup &athis stacker",
                                "",
                                "&7(( Allowed: &a" + bool + "&7 ))"
                        ))
                        .build().make();
            }
        };

        editPermsPermButton = new Button() {

            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                boolean bool = perms.contains(StackerPermission.EDIT_PERMISSIONS);
                if (bool) block.removePermission(StackerPermission.EDIT_PERMISSIONS);
                else block.addPermission(StackerPermission.EDIT_PERMISSIONS);
                redraw();
            }

            @Override
            public ItemStack getItem() {
                boolean bool = perms.contains(StackerPermission.EDIT_PERMISSIONS);
                return ItemCreator.of(getMaterial(bool))
                        .name("&a&lEdit Permissions")
                        .lores(Arrays.asList(
                                "",
                                "&2&lDescription",
                                "&aAllows your island to",
                                "&2Edit &aThe permissions of this stacker",
                                "",
                                "&7(( Allowed: &a" + bool + "&7 ))"
                        ))
                        .build().make();
            }
        };

        new ButtonReturnBack(parent);
    }


    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 0) return addBlocksPermButton.getItem();
        if (slot == 1) return removeBlocksPermButton.getItem();
        if (slot == 2) return openStackerPermButton.getItem();
        if (slot == 3) return pickupStackerPermButton.getItem();
        if (slot == 4) return editPermsPermButton.getItem();

        return CompMaterial.BLACK_STAINED_GLASS_PANE.toItem();
    }

    private CompMaterial getMaterial(boolean bool) {
        return bool ? CompMaterial.GREEN_WOOL : CompMaterial.RED_WOOL;
    }


}
