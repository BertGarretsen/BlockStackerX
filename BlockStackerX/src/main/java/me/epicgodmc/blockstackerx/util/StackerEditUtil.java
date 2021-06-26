package me.epicgodmc.blockstackerx.util;

import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//public class StackerEditUtil {
//
//    private static Map<UUID, StackerSettings> editors = new HashMap<>();
//
//
//    public static boolean isEditing(UUID uuid) {
//        return editors.containsKey(uuid);
//    }
//
//    public static StackerSettings getDraft(UUID uuid) {
//        return editors.get(uuid);
//    }
//
//    public static void setDraft(UUID uuid, StackerSettings draft) {
//        editors.put(uuid, draft);
//    }
//
//    public static void clear(UUID uuid) {
//        if (editors.containsKey(uuid)) {
//            StackerRegister.getInstance().registerSettings(editors.get(uuid));
//            editors.remove(uuid);
//        }
//    }
//
//}
