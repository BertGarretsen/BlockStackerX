package me.epicgodmc.blockstackerx.menu.common;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.concurrent.Callable;

/**
 * Created by Bert on 02 Oct 2021
 * Copyright © EpicGodMC
 */
public final class PermableButtonMenu extends Button {

    /**
     * Sometimes you need to allocate data when you create the button,
     * but these data are not yet available when you make new instance of this button
     * <p>
     * Use this helper to set them right before showing the button
     */
    private final Callable<Menu> menuLateBind;

    /**
     * used to check if player can open menu
     */
    private boolean canOpen;

    /**
     * Message send to player if unable to open
     */
    private String deniedMessage;

    /**
     * The menu this button opens
     */
    private final Menu menuToOpen;

    /**
     * The icon of this button
     */
    @Getter
    private final ItemStack item;

    /**
     * Create a new instanceof using {@link Menu#newInstance()} when showing the menu?
     */
    private final boolean newInstance;

    /**
     * Create a new button that triggers another menu
     *
     * @param menuClass
     * @param material
     * @param name
     * @param lore
     */
    public PermableButtonMenu(final Class<? extends Menu> menuClass, final boolean canOpen, final String deniedMessage, final CompMaterial material, final String name, final String... lore) {
        this(null, () -> ReflectionUtil.instantiate(menuClass), ItemCreator.of(material, name, lore).hideTags(true).build().make(), false, canOpen, deniedMessage);
    }

    /**
     * Create a new button that triggers another menu
     *
     * @param menuLateBind
     * @param item
     */
    public PermableButtonMenu(final Callable<Menu> menuLateBind, final ItemCreator.ItemCreatorBuilder item, final boolean canOpen, final String deniedMessage) {
        this(null, menuLateBind, item.hideTags(true).build().make(), false, canOpen, deniedMessage);
    }

    /**
     * Create a new button that triggers another menu
     *
     * @param menuLateBind
     * @param item
     */
    public PermableButtonMenu(final Callable<Menu> menuLateBind, final ItemStack item, final boolean canOpen, final String deniedMessage) {
        this(null, menuLateBind, item, false, canOpen, deniedMessage);
    }

    /**
     * Create a new button that triggers another menu
     *
     * @param menu
     * @param material
     * @param name
     * @param lore
     */
    public PermableButtonMenu(final Menu menu, final boolean canOpen, final String deniedMessage, final CompMaterial material, final String name, final String... lore) {
        this(menu, ItemCreator.of(material, name, lore), canOpen, deniedMessage);
    }

    /**
     * Create a new button that triggers another menu
     *
     * @param menu
     * @param item
     */
    public PermableButtonMenu(final Menu menu, final ItemCreator.ItemCreatorBuilder item, final boolean canOpen, final String deniedMessage) {
        this(menu, null, item.hideTags(true).build().make(), false, canOpen, deniedMessage);
    }

    /**
     * Create a new button that triggers another menu
     *
     * @param menu
     * @param item
     */
    public PermableButtonMenu(final Menu menu, final ItemStack item, final boolean canOpen, final String deniedMessage) {
        this(menu, null, item, false, canOpen, deniedMessage);
    }

    public PermableButtonMenu(final Menu menu, final ItemStack item, final boolean newInstance, final boolean canOpen, final String deniedMessage) {
        this(menu, null, item, newInstance, canOpen, deniedMessage);
    }

    // Private constructor
    private PermableButtonMenu(final Menu menuToOpen, final Callable<Menu> menuLateBind, final ItemStack item, final boolean newInstance, boolean canOpen, final String deniedMessage) {
        this.menuToOpen = menuToOpen;
        this.menuLateBind = menuLateBind;
        this.item = item;
        this.newInstance = newInstance;
        this.canOpen = canOpen;
        this.deniedMessage = deniedMessage;
    }

    /**
     * Automatically display another menu when the button is clicked
     */
    @Override
    public void onClickedInMenu(final Player pl, final Menu menu, final ClickType click) {
        if (!canOpen) {
            if (deniedMessage != null && !deniedMessage.isEmpty()) Common.tell(pl, deniedMessage);
            return;
        }
        if (menuLateBind != null) {
            Menu menuToOpen = null;

            try {
                menuToOpen = menuLateBind.call();
            } catch (final Exception ex) {
                ex.printStackTrace();

                return;
            }

            if (newInstance)
                menuToOpen = menuToOpen.newInstance();

            menuToOpen.displayTo(pl);

        } else {
            Valid.checkNotNull(menuToOpen, "Report / ButtonTrigger requires either 'late bind menu' or normal menu to be set!");

            if (newInstance)
                menuToOpen.newInstance().displayTo(pl);
            else
                menuToOpen.displayTo(pl);
        }
    }
}
