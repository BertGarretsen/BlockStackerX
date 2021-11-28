package me.epicgodmc.blockstackerx.storage.impl;

import lombok.Getter;
import me.epicgodmc.blockstackerx.StackerPlugin;
import me.epicgodmc.blockstackerx.stacker.StackerBlock;
import me.epicgodmc.blockstackerx.storage.IslandCache;
import me.epicgodmc.blockstackerx.storage.IslandStorage;
import me.imspooks.baos.Baos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Bert on 22 Sep 2021
 * Copyright © EpicGodMC
 */
public class ByteIslandStorage implements IslandStorage {


    @Getter private final Path storage;

    public ByteIslandStorage() {
        this.storage = StackerPlugin.getInstance().getDataFolder().toPath().resolve("ByteData");
    }

    @Override
    public void save(IslandCache src) throws IOException {
        Path dataFile = storage.resolve(src.getUUID() + ".dat");

        if (!Files.exists(dataFile)) {
            createFileIfNotExists(dataFile);
        }

        try {
            byte[] bytes = Baos.write(out -> {
                out.writeString(src.getUUID());

                out.writeInt(src.getStackers().size());
                for (StackerBlock value : src.getStackers().values()) {
                    value.serialize(out);
                }
            });


            Files.write(dataFile, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IslandCache load(String uuid) {
        Path dataFile = storage.resolve(uuid + ".dat");


        if (!Files.exists(dataFile)) {
            return new IslandCache(uuid);
        }

        AtomicReference<IslandCache> cache = new AtomicReference<>();
        try {
            byte[] bytes = Files.readAllBytes(dataFile);
            Baos.read(bytes, in -> {
                cache.set(new IslandCache(in.readString()));

                int length = in.readInt();
                for (int i = 0; i < length; i++) {
                    StackerBlock block = new StackerBlock();
                    block.deserialize(in);

                    if (block.getSettings() != null) {
                        cache.get().getStackers().put(block.getLocation(), block);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            cache.set(null);
        }
        return cache.get();
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
     * Creates a dat file for the IslandCache if it does not exist
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

}
