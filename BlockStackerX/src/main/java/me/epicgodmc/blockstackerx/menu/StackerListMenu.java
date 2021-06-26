package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;

public class StackerListMenu extends MenuPagged<StackerSettings> {


    public StackerListMenu() {
        super(StackerRegister.getLoadedSettings());
    }


    @Override
    protected ItemStack convertToItemStack(StackerSettings settings) {
        return ItemCreator.of(settings.getDefaultMaterial(), "&c&l" + settings.getIdentifier(), "", "&7((&cLeft-click &7to spawn new stacker&7))", "&7((&cRight-Click &7to edit stacker settings&7))").build().make();
    }

    @Override
    protected void onPageClick(Player player, StackerSettings settings, ClickType click) {
        if (click.isLeftClick())
        {
            player.getInventory().addItem(settings.getNewStacker());
            animateTitle("Stacker Added into your inventory");
        }
        if (click.isRightClick())
        {
            new StackerEditMenu(settings).displayTo(player);
        }
    }


}
