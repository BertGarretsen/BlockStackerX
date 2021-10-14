package me.epicgodmc.blockstackerx.util;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.mineacademy.fo.model.SimpleHologram;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Bert on 14 Oct 2021
 * Copyright © EpicGodMC
 */
public class SimpleTextHologram extends SimpleHologram {


    public SimpleTextHologram(Location spawnLocation) {
        super(spawnLocation);
    }

    @Override
    protected Entity createEntity() {
        final ArmorStand armorStand = this.getLastTeleportLocation().getWorld().spawn(this.getLastTeleportLocation(), ArmorStand.class);

        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);

        return armorStand;
    }


    public void updateLore() {
        this.removeLore();
        try {
            DRAW_LORE_METHOD.invoke(this, this.getLocation());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }


    private static Method DRAW_LORE_METHOD;

    static {
        try {
            DRAW_LORE_METHOD = SimpleHologram.class.getDeclaredMethod("drawLore", Location.class);
            DRAW_LORE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
