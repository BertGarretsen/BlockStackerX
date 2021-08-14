package me.epicgodmc.blockstackerx.storage;

import lombok.Getter;
import me.epicgodmc.blockstackerx.StackerBlock;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.gson.GlobalGson;
import me.epicgodmc.blockstackerx.util.SimpleLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.collection.expiringmap.ExpirationPolicy;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class IslandCache {

    private static ExpiringMap<UUID, IslandCache> cacheMap = ExpiringMap.builder()
            .expiration(10, TimeUnit.MINUTES)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .asyncExpirationListener((k, v) ->
                    ((IslandCache) v).save()).build();

    private final Path storage;
    @Getter private final UUID UUID;
    @Getter private StrictMap<SimpleLocation, StackerBlock> stackers;


    public IslandCache(UUID islandUUID) {
        this.UUID = islandUUID;
        this.storage = StackerPlugin.getInstance().getDataFolder().toPath().resolve("IslandData").resolve(islandUUID.toString() + ".json");
    }

    public boolean hasStackerAt(SimpleLocation location) {
        return this.stackers.contains(location);
    }

    public boolean hasStackerAt(Location location) {
        return this.stackers.contains(SimpleLocation.of(location));
    }

    public StackerBlock getStackerAt(SimpleLocation simpleLocation)
    {
        return this.stackers.getOrDefault(simpleLocation, null);
    }

    public StackerBlock getStackerAt(Location location)
    {
        return this.stackers.getOrDefault(SimpleLocation.of(location), null);
    }

    public void addStacker(StackerBlock stackerBlock)
    {
        this.stackers.put(stackerBlock.getSimpleLocation(), stackerBlock);
    }


    /**
     * Responsible for either returning/loading/creating IslandCaches asynchronously
     *
     * @param uuid universal unique identifier of the island in question
     * @return
     */
    public static IslandCache getCache(UUID uuid) {
        IslandCache cache = cacheMap.getOrDefault(uuid, null);
        if (cache == null) {
            Path path = StackerPlugin.getInstance().getDataFolder().toPath().resolve("IslandData").resolve(uuid.toString() + ".json");
            if (!Files.exists(path)) {
                CompletableFuture<IslandCache> asyncCache = new CompletableFuture<>();

                new Thread(() ->
                {
                    try {
                        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                        asyncCache.complete(GlobalGson.GSON.fromJson(json, IslandCache.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                        asyncCache.complete(null);
                    }
                }).start();

                try {
                    cache = asyncCache.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                cache = new IslandCache(uuid);
            }
            cacheMap.put(uuid, cache);
        }
        return cache;
    }

    /**
     * Saves the object to its json file
     */
    public void save() {
        try {
            StackerPlugin.sendDebug("Island Cache " + this.UUID + " has been unloaded and saved.");
            this.createFileIfNotExists();
            Files.write(this.storage, GlobalGson.GSON.toJson(this).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all loaded objects to their corresponding json files
     */
    public static void saveAll() {
        cacheMap.forEach((k, v) ->
                v.save());
    }

    /**
     * Creates a json file for the IslandCache if it does not exist
     * Automatically creates parent file if it does not exist
     *
     * @throws IOException
     */
    public void createFileIfNotExists() throws IOException {
        if (!Files.exists(this.storage)) {
            if (!Files.exists(this.storage.getParent())) {
                Files.createDirectory(this.storage.getParent());
            }
            Files.createFile(this.storage);
        }
    }

    /**
     * Cleans the cache in case of a reload
     */
    public static void clean() {
        cacheMap.clear();
    }

}
