package me.epicgodmc.blockstackerx.hook.hologram.impl;

import me.epicgodmc.blockstackerx.hook.hologram.StackerHologram;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMetadata;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * Created by Bert on 16 Oct 2021
 * Copyright © EpicGodMC
 */
public class BSXStackerHologram extends StackerHologram implements Listener {

    private WeakReference<ArmorStand> hologram;
    private StackerBlock owner;

    public BSXStackerHologram(String format, StackerBlock owner) {
        super(format);
        this.owner = owner;
    }

    @Override
    public void create(int value, Location location) {
        this.updateArmorstand();

        ArmorStand stand = getHologramEntity();
        if (!this.isSpawned()) {
            stand = location.getWorld().spawn(location, ArmorStand.class);
        }

        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setMarker(true);

        CompMetadata.setMetadata(stand, "stacker-hologram", this.owner.getLocation().toString());

        this.hologram = new WeakReference<>(stand);
        this.update(value);
    }

    @Override
    public boolean isSpawned() {
        ArmorStand stand = this.getHologramEntity();
        return stand != null && stand.isValid();
    }

    @Override
    public void update(int newValue) {
        this.update(0, getFormatReplaced(newValue));
    }

    @Override
    public void update(int line, String text) {
        ArmorStand stand = this.getHologramEntity();
        if (stand != null) {
            stand.setCustomName(Common.colorize(text));
        }
    }

    @Override
    public void delete() {
        if (this.isSpawned()) {
            this.getHologramEntity().remove();
        }
    }

    @Nullable
    private ArmorStand getHologramEntity() {
        this.updateArmorstand();

        if (this.hologram == null) {
            return null;
        }
        return this.hologram.get();
    }

    private void updateArmorstand() {
        for (Entity nearbyEntity : this.owner.getLocation().getNearbyEntities(2, 2, 2)) {
            if (nearbyEntity instanceof ArmorStand) {
                ArmorStand stand = (ArmorStand) nearbyEntity;

                String value = CompMetadata.getMetadata(stand, "stacker-hologram");
                if (value.equals(this.owner.getLocation().toString())) {
                    this.hologram = new WeakReference<>(stand);
                }
            }
        }
    }
}
