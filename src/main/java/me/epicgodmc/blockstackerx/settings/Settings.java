package me.epicgodmc.blockstackerx.settings;

import org.bukkit.event.block.Action;
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
    public static Action STACKER_ADD_ACTION;
    public static Action STACKER_TAKE_ACTION;
    public static String STORAGE_TYPE;
    public static String HOLOGRAM_PROVIDER_TYPE;

    private static void init()
    {
        DEBUG = getBoolean("DebugMessages");
        STACKER_ADD_ACTION = Enum.valueOf(Action.class, getString("Stacker_Add_Action"));
        STACKER_TAKE_ACTION = Enum.valueOf(Action.class, getString("Stacker_Take_Action"));
        STORAGE_TYPE = getString("StorageType");
        HOLOGRAM_PROVIDER_TYPE = getString("HologramType");
    }

}
