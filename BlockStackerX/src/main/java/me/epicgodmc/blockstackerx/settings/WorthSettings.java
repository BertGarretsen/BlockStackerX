package me.epicgodmc.blockstackerx.settings;

import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bert on 17 Sep 2021
 * Copyright © EpicGodMC
 */
public class WorthSettings extends YamlConfig
{

    private static double DEFAULT_WORTH;
    private static Map<CompMaterial, Double> worths = new HashMap<>();

    public WorthSettings()
    {
        loadConfiguration("Worth.yml", "Worth.yml");


    }

    @Override
    protected void onLoadFinish() {
        super.onLoadFinish();


        DEFAULT_WORTH = getDouble("DefaultWorth");

        SerializedMap worths = getMap("Worths");

        if (worths.isEmpty()) return;

        for (String mat : worths.keySet()) {

            CompMaterial compMaterial = CompMaterial.fromString(mat.toUpperCase());
            double worth = worths.getDouble(mat);
            WorthSettings.worths.put(compMaterial, worth);
        }
    }

    public static double getWorth(CompMaterial compMaterial)
    {
        if (worths.containsKey(compMaterial)) return worths.get(compMaterial);
        else return DEFAULT_WORTH;
    }
}
