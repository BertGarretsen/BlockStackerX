package me.epicgodmc.blockstackerx;

import org.bukkit.NamespacedKey;
import org.bukkit.conversations.ConversationPrefix;
import org.mineacademy.fo.Common;

public class Global
{

    public static final NamespacedKey STACKER_KEY = new NamespacedKey(StackerPlugin.getInstance(), "Stacker");
    public static final ConversationPrefix conversationPrefix = context -> Common.colorize("&c&lBlockStacker&8&lX &c» ");

}
