package me.epicgodmc.blockstackerx.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.epicgodmc.blockstackerx.storage.impl.ByteIslandStorage;
import me.epicgodmc.blockstackerx.storage.impl.JsonIslandStorage;

/**
 * Created by Bert on 22 Sep 2021
 * Copyright © EpicGodMC
 */
@RequiredArgsConstructor
@Getter
public enum IslandStorageType {

    JSON(new JsonIslandStorage()),
    BYTE(new ByteIslandStorage())
    ;

    private final IslandStorage storage;
}
