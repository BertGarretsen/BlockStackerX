package me.epicgodmc.blockstackerx.storage;

import java.io.IOException;

/**
 * Created by Bert on 22 Sep 2021
 * Copyright © EpicGodMC
 */
public interface IslandStorage {


    void save(IslandCache islandCache) throws IOException;

    void saveAll();

    IslandCache load(String uuid);

}
