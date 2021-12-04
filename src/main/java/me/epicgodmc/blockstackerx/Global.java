package me.epicgodmc.blockstackerx;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.conversations.ConversationPrefix;
import org.mineacademy.fo.Common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global
{

    public static final NamespacedKey STACKER_KEY = new NamespacedKey(StackerPlugin.getInstance(), "Stacker");
    public static final ConversationPrefix conversationPrefix = context -> Common.colorize("&c&lBlockStacker&8&lX &c» ");

    public static final ExecutorService ASYNC_SERVICE = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("BlockStackerX Pool Thread #%1$d").build());
}
