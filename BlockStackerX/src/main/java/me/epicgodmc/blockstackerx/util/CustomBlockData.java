package me.epicgodmc.blockstackerx.util;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by Bert on 01 Oct 2021
 * Copyright © EpicGodMC
 */
public class CustomBlockData implements PersistentDataContainer {

    private final PersistentDataContainer pdc;
    private final Chunk chunk;
    private final NamespacedKey key;


    public CustomBlockData(final @NotNull Block block, final @NotNull Plugin plugin) {
        this.chunk = block.getChunk();
        this.key = new NamespacedKey(plugin, getKey(block));
        this.pdc = getPersistentDataContainer();
    }


    @NotNull
    private PersistentDataContainer getPersistentDataContainer() {
        final PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        final PersistentDataContainer blockPDC;

        if (chunkPDC.has(key, PersistentDataType.TAG_CONTAINER)) {
            blockPDC = chunkPDC.get(key, PersistentDataType.TAG_CONTAINER);
            assert blockPDC != null;
            return blockPDC;
        }
        blockPDC = chunkPDC.getAdapterContext().newPersistentDataContainer();
        chunkPDC.set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return blockPDC;
    }


    @NotNull
    private static String getKey(@NotNull Block b) {
        final int x = b.getX() & 0x000F;
        final int y = b.getY() & 0x00FF;
        final int z = b.getZ() & 0X000F;
        return String.format("x%dy%dz%d", x, y, z);
    }

    public void clear() {
        if (pdc.has(key, PersistentDataType.TAG_CONTAINER)) {
            pdc.remove(key);
        }
        save();
    }

    private void save() {
        if (pdc.isEmpty()) {
            chunk.getPersistentDataContainer().remove(key);
        } else {
            chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
        }
    }


    @Override
    public <T, Z> void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        pdc.set(namespacedKey, persistentDataType, z);
        save();
    }

    @Override
    public <T, Z> boolean has(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return pdc.has(namespacedKey, persistentDataType);
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return pdc.get(namespacedKey, persistentDataType);
    }

    @Override
    public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        return pdc.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @Override
    public @NotNull Set<NamespacedKey> getKeys() {
        return pdc.getKeys();
    }

    @Override
    public void remove(@NotNull NamespacedKey namespacedKey) {
        pdc.remove(namespacedKey);
        save();
    }

    @Override
    public boolean isEmpty() {
        return pdc.isEmpty();
    }

    @Override
    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        return pdc.getAdapterContext();
    }
}
