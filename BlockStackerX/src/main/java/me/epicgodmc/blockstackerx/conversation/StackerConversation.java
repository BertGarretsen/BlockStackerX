package me.epicgodmc.blockstackerx.conversation;

import me.epicgodmc.blockstackerx.Global;
import me.epicgodmc.blockstackerx.menu.StackerEditMenu;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.conversation.SimpleConversation;

public abstract class StackerConversation extends SimpleConversation
{

    @Override
    protected boolean reopenMenu() {
        return false;
    }

    @Override
    protected ConversationPrefix getPrefix() {
        return Global.conversationPrefix;
    }


    public void openInv(StackerSettings settings, Player player)
    {
        Common.runLater(1, () -> new StackerEditMenu(settings).displayTo(player));
    }

}
