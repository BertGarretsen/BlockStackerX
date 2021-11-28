package me.epicgodmc.blockstackerx.storage.impl;

import lombok.Getter;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.gson.GlobalGson;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.storage.IslandStorage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Bert on 22 Sep 2021
 * Copyright © EpicGodMC
 */
public class JsonIslandStorage implements IslandStorage {

    @Getter private final Path storage;

    public JsonIslandStorage() {
        this.storage = StackerPlugin.getInstance().getDataFolder().toPath().resolve("JsonData");
    }

    @Override
    public void save(IslandCache cache) {
        try {
            StackerPlugin.sendDebug("Island Cache " + cache.getUUID() + " has been unloaded and saved.");
            createFileIfNotExists(storage.resolve(cache.getUUID() + ".json"));
            Files.write(storage.resolve(cache.getUUID() + ".json"), GlobalGson.GSON.toJson(cache).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveAll() {
        IslandCache.getCacheMap().forEach((k, v) ->
        {
            try {
                v.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void delete(String uuid) {
        try {
            Files.delete(storage.resolve(uuid + ".dat"));
        } catch (IOException ignored) {
        }
    }


    /**
     * Creates a json file for the IslandCache if it does not exist
     * Automatically creates parent file if it does not exist
     *
     * @throws IOException
     */
    public void createFileIfNotExists(Path storage) throws IOException {
        if (!Files.exists(storage)) {
            if (!Files.exists(storage.getParent())) {
                Files.createDirectory(storage.getParent());
            }
            Files.createFile(storage);
        }
    }

    @Override
    public IslandCache load(String uuid) {
        Path islandData = this.storage.resolve(uuid + ".json");

        if (!Files.exists(islandData)) {
            return new IslandCache(uuid);
        }

        CompletableFuture<IslandCache> asyncCache = new CompletableFuture<>();

        new Thread(() ->
        {
            try {
                String json = new String(Files.readAllBytes(islandData), StandardCharsets.UTF_8);
                asyncCache.complete(GlobalGson.GSON.fromJson(json, IslandCache.class));
            } catch (IOException e) {
                e.printStackTrace();
                asyncCache.complete(null);
            }
        }).start();

        try {
            return asyncCache.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }
}
