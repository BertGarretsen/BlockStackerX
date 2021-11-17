package me.epicgodmc.blockstackerx.storage;

import lombok.Getter;
import lombok.Setter;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.util.StackerLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.collection.expiringmap.ExpirationPolicy;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class IslandCache {


    @Getter
    private static transient ExpiringMap<String, IslandCache> cacheMap = ExpiringMap.builder()
            .expiration(10, TimeUnit.MINUTES)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .asyncExpirationListener((k, v) ->
            {
                try {
                    ((IslandCache) v).save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).build();

    @Getter private final String UUID;
    @Getter @Setter private StrictMap<StackerLocation, StackerBlock> stackers;


    public IslandCache(String islandUUID) {
        this.UUID = islandUUID;
        this.stackers = new StrictMap<>();
    }

    public boolean hasStackerAt(StackerLocation location) {
        return this.stackers.contains(location);
    }

    public boolean hasStackerAt(Location location) {
        return this.stackers.contains(new StackerLocation(location));
    }

    public StackerBlock getStackerAt(StackerLocation stackerLocation) {
        return this.stackers.getOrDefault(stackerLocation, null);
    }

    public StackerBlock getStackerAt(Location location) {
        return this.stackers.getOrDefault(new StackerLocation(location), null);
    }

    public void addStacker(StackerBlock stackerBlock) {
        this.stackers.put(stackerBlock.getLocation(), stackerBlock);
    }

    public void deleteStacker(StackerLocation stackerLocation) {
        this.stackers.remove(stackerLocation);
    }

    public void deleteStacker(Location location) {
        this.stackers.remove(new StackerLocation(location));
    }

    public ItemStack deleteStacker(StackerBlock stackerBlock) {
        ItemStack result = stackerBlock.getItemForm();
        stackerBlock.delete();
        this.deleteStacker(stackerBlock.getLocation());
        return result;
    }

    public void addAllStackers(Iterable<StackerBlock> stackerBlocks) {
        stackerBlocks.forEach(this::addStacker);
    }



    public static IslandCache getCache(Player player)
    {
        String uuid = StackerPlugin.getInstance().getHookManager().getIslandID(player);
        return getCache(uuid);
    }


    /**
     * Responsible for either returning/loading/creating IslandCaches asynchronously
     *
     * @param uuid universal unique identifier of the island in question
     * @return
     */
    public static IslandCache getCache(String uuid) {
        IslandCache cache = cacheMap.getOrDefault(uuid, null);
        if (cache == null) {
            cache = StackerPlugin.getInstance().getIslandStorage().load(uuid);
            cacheMap.put(uuid, cache);
        }
        return cache;
    }


    public void saveAndUnload() throws IOException {
        save();
        stackers.values().forEach(StackerBlock::unload);
    }

    /**
     * Saves the object to its file
     */
    public void save() throws IOException {
        StackerPlugin.getInstance().getIslandStorage().save(this);
    }

    public static void saveAndUnloadAll() {
        cacheMap.forEach((k, v) -> {
            try {
                v.saveAndUnload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clean();
    }

    /**
     * Cleans the cache in case of a reload
     */
    public static void clean() {
        cacheMap.clear();
    }


}
