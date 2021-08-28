package me.epicgodmc.blockstackerx.settings;

import org.mineacademy.fo.settings.SimpleSettings;

/**
 * Created by Bert on 14 Aug 2021
 * Copyright © EpicGodMC
 */
public class Settings extends SimpleSettings
{


    @Override
    protected int getConfigVersion() {
        return 1;
    }


    public static Boolean DEBUG;

    private static void init()
    {
        DEBUG = getBoolean("DebugMessages");

    }

}
