package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.menu.common.PermableButtonMenu;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.menu.common.DefinedButton;
import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.common.InventorySettings;
import me.epicgodmc.blockstackerx.stacker.StackerPermission;
import me.epicgodmc.blockstackerx.util.InventoryItem;
import me.epicgodmc.blockstackerx.util.InventoryUtils;
import me.epicgodmc.blockstackerx.util.StackerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;

import java.util.*;

/**
 * Created by Bert on 17 Sep 2021
 * Copyright © EpicGodMC
 */
public class StackerMenu extends Menu {

    //Pickup
    //Information
    //deposit-inv
    //withdraw-inv

    private final HashMap<Integer, Button> buttonMap = new HashMap<>();
    private InventorySettings inventorySettings;
    private final StackerBlock block;

    private Map<String, Object> placeHolders = new HashMap<>();

    public StackerMenu(UUID player, StackerBlock block) {
        this.block = block;

        if (block.getSettings() == null) {
            return;
        }
        this.inventorySettings = block.getSettings().getInventorySettings();

        setTitle(inventorySettings.getTitle());
        setSize(inventorySettings.getSlots());

        placeHolders.put("owner", block.getPlayerOwner().getName());
        placeHolders.put("value", block.getValue());
        placeHolders.put("levels", block.calculateLevels());
        placeHolders.put("matvalue", block.getMaterialValue());

        InventoryItem pickupItem = inventorySettings.getButton("Pickup");
        pickupItem.setPlaceholders(placeHolders);
        Button pickupButton = new DefinedButton(pickupItem.buildAndClear()) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                if (!block.hasPermission(player, StackerPermission.PICKUP_STACKER)) {
                    Common.tell(player, Localization.Stacker_Actions.STACKER_NO_PERMISSION);
                    return;
                }

                player.closeInventory();
                PlayerUtil.addItems(player.getInventory(), block.getSettings().getUsedStacker(block));
                block.delete();
            }
        };

        InventoryItem informationItem = inventorySettings.getButton("Information");
        informationItem.setPlaceholders(placeHolders);
        Button informationButton = new DefinedButton(informationItem.buildAndClear()) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
            }
        };

        InventoryItem depositItem = inventorySettings.getButton("DepositAll");
        depositItem.setPlaceholders(placeHolders);
        Button depositButton = new DefinedButton(depositItem.buildAndClear()) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (block.hasStackMaterial()) {
                    int count = StackerUtils.countSimilar(player.getInventory(), block.getStackMaterial().toItem());
                    if (block.canAddValue(count)) {
                        boolean taken = StackerUtils.takeClean(player, block.getStackMaterial().toItem(), count);
                        if (taken) {
                            block.incrementValue(count);
                            Common.tell(player, Localization.Stacker_Actions.BLOCK_ADDED.replace("{amount}", String.valueOf(count)).replace("{block}", ItemUtil.bountifyCapitalized(block.getStackMaterial())));
                        } else {
                            Common.tell(player, Localization.Stacker_Actions.FAILED_TO_DEPOSIT);
                        }
                    } else {
                        int fittingCount = block.getSpaceLeft();
                        boolean taken = StackerUtils.takeClean(player, block.getStackMaterial().toItem(), fittingCount);
                        if (taken) {
                            block.incrementValue(fittingCount);
                            Common.tell(player, Localization.Stacker_Actions.BLOCK_ADDED.replace("{amount}", String.valueOf(fittingCount)).replace("{block}", ItemUtil.bountifyCapitalized(block.getStackMaterial())));
                        }else{
                            Common.tell(player, Localization.Stacker_Actions.FAILED_TO_DEPOSIT);
                        }
                    }
                    redrawInv();
                } else Common.tell(player, Localization.Stacker_Actions.NO_TYPE_FOUND);
            }
        };

        InventoryItem withdrawItem = inventorySettings.getButton("WithdrawAll");
        withdrawItem.setPlaceholders(placeHolders);
        Button withdrawButton = new DefinedButton(withdrawItem.buildAndClear()) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (block.hasStackMaterial()) {
                    int inventorySpace = InventoryUtils.roomLeft(player.getInventory(), block.getStackMaterial());
                    int stored = block.getValue();

                    if (inventorySpace >= stored) {
                        PlayerUtil.addItems(player.getInventory(), block.getStackMaterial().toItem(stored));
                        block.setValue(0);
                        Common.tell(player, Localization.Stacker_Actions.BLOCK_SUBTRACTED.replace("{amount}", String.valueOf(stored)).replace("{block}", ItemUtil.bountifyCapitalized(block.getStackMaterial())));
                    } else {
                        PlayerUtil.addItems(player.getInventory(), block.getStackMaterial().toItem(inventorySpace));
                        block.subtractValue(inventorySpace);
                        Common.tell(player, Localization.Stacker_Actions.BLOCK_SUBTRACTED.replace("{amount}", String.valueOf(inventorySpace)).replace("{block}", ItemUtil.bountifyCapitalized(block.getStackMaterial())));
                    }
                    redrawInv();
                } else Common.tell(player, Localization.Stacker_Actions.NO_TYPE_FOUND);
            }
        };

        InventoryItem permissionItem = inventorySettings.getButton("Permissions");
        permissionItem.setPlaceholders(placeHolders);
        Button permissionSettings = new PermableButtonMenu(new StackerPermMenu(this, block), permissionItem.buildAndClear(), block.hasPermission(player, StackerPermission.EDIT_PERMISSIONS), Localization.Stacker_Actions.STACKER_NO_PERMISSION);


        buttonMap.put(pickupItem.getSlot(), pickupButton);
        buttonMap.put(informationItem.getSlot(), informationButton);
        buttonMap.put(depositItem.getSlot(), depositButton);
        buttonMap.put(withdrawItem.getSlot(), withdrawButton);
        buttonMap.put(permissionItem.getSlot(), permissionSettings);
    }


    public void redrawInv() {
        placeHolders.put("owner", block.getPlayerOwner().getName());
        placeHolders.put("value", block.getValue());
        placeHolders.put("levels", block.calculateLevels());
        placeHolders.put("matvalue", block.getMaterialValue());


        InventoryItem informationItem = inventorySettings.getButton("Information");
        informationItem.setPlaceholders(placeHolders);
        Button informationButton = new DefinedButton(informationItem.buildAndClear()) {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
            }
        };

        buttonMap.put(informationItem.getSlot(), informationButton);

        redraw();
    }

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttonMap.values());
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (buttonMap.containsKey(slot)) return buttonMap.get(slot).getItem();
        return null;
    }

}
