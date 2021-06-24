package me.epicgodmc.blockstackerx.settings;

import org.mineacademy.fo.settings.SimpleLocalization;

public class Localization extends SimpleLocalization
{

    @Override
    protected int getConfigVersion() {
        return 1;
    }

    public static class Stacker_Actions
    {
        public static String INVALID_BLOCK;
        public static String MODIFY_DENY;
        public static String MATERIAL_CHOSEN;
        public static String BLOCK_ADDED;
        public static String BLOCK_SUBTRACTED;
        public static String MAX_STORAGE_REACHED;
        public static String MIN_STORAGE_REACHED;
        public static String NO_PERMISSION;
        public static String ERROR;
        public static String MATERIAL_CONTAINS_NBT;
        public static String NO_ISLAND_FOUND;

        private static void init()
        {
            pathPrefix("Stacker_Actions");

            INVALID_BLOCK = getString("Invalid_Block");
            MODIFY_DENY = getString("Modify_Deny");
            MATERIAL_CHOSEN = getString("Material_Chosen");
            BLOCK_ADDED = getString("Block_Added");
            BLOCK_SUBTRACTED = getString("Block_Subtracted");
            MAX_STORAGE_REACHED = getString("Max_Storage_Reached");
            MIN_STORAGE_REACHED = getString("Min_Storage_Reached");
            NO_PERMISSION = getString("No_Permission");
            ERROR = getString("Error");
            MATERIAL_CONTAINS_NBT = getString("Material_Contains_NBT");
            NO_ISLAND_FOUND = getString("No_Island_Found");

        }
    }

    public static class Command
    {
        public static String STACKER_CREATED;
        public static String STACKER_NOT_FOUND;
        public static String STACKER_GIVEN;
        public static String STACKER_RECEIVE;
        public static String STACKER_ALREADY_EXISTS;

        private static void init()
        {
            pathPrefix("Commands");

            STACKER_CREATED = getString("Stacker_Created");
            STACKER_NOT_FOUND = getString("Stacker_Not_Found");
            STACKER_GIVEN = getString("Stacker_Given");
            STACKER_RECEIVE = getString("Stacker_Receive");
            STACKER_ALREADY_EXISTS = getString("Stacker_Already_Exists");
        }

    }

    public static class Player
    {
        public static String INVENTORY_FULL;

        private static void init()
        {
            pathPrefix("Player");

            INVENTORY_FULL = getString("Inventory_Full");
        }
    }

    public static class Conversation
    {
        public static String CANCELLED;

        private static void init()
        {
            pathPrefix("Conversation");

            CANCELLED = getString("Cancelled");
        }
    }
}
