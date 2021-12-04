package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class StackerListMenu extends MenuPagged<StackerSettings> {


    public StackerListMenu() {
        super(StackerRegister.getInstance().getLoadedSettingsSorted());
    }


    @Override
    protected ItemStack convertToItemStack(StackerSettings settings) {
        String fancyState = ItemUtil.bountifyCapitalized(settings.getState());
        if (settings.getState() == StackerSettings.State.ACTIVE) {
            return ItemCreator
                    .of(CompMaterial.fromItem(settings.getNewStacker())
                            , "&a&l" + settings.getIdentifier() + " &8((&a" + fancyState + "&8))"
                            , ""
                            , "&7((&aLeft-click&7)) to spawn new stacker"
                            , "&7((&aRight-Click&7)) to edit stacker settings")
                    .build().make();
        }
        return ItemCreator
                .of(settings.getDefaultMaterial()
                        , "&c&l" + settings.getIdentifier() + " &8((&c" + fancyState + "&8))"
                        , ""
                        , "&7((&cLeft-click&7)) to spawn new stacker"
                        , "&7((&cRight-Click&7)) to edit stacker settings")
                .build().make();
    }

    @Override
    protected void onPageClick(Player player, StackerSettings settings, ClickType click) {
        if (click.isLeftClick()) {
            player.getInventory().addItem(settings.getNewStacker());
            animateTitle("Stacker Added into your inventory");
        }
        if (click.isRightClick()) {
            new StackerEditMenu(settings).displayTo(player);
        }
    }
}
