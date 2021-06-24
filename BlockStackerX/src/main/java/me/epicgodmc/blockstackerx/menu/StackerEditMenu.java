package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.conversation.StackerCreateConversation;
import me.epicgodmc.blockstackerx.conversation.stacker.IdentifierPrompt;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class StackerEditMenu extends Menu {

    /*buttons

    identifier
    hologram offset
    value format
    max storage
    team stacking
    available blocks
    used

     */

    private final Button idButton;

    public StackerEditMenu(StackerSettings draft)
    {
        setTitle("&c&lEditing Stacker &7(("+draft.getIdentifier()+"))");
        setSize(9*4);

        //temp
        setSlotNumbersVisible();
        //

        idButton = new ButtonConversation(new IdentifierPrompt(), ItemCreator.of(CompMaterial.INK_SAC,
                "&c&lIdentifier",
                "",
                "&cexample: &7\"superstacker\"",
                "",
                "&cValue: &7"+draft.getIdentifier()));

    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 9*1 + 1)
        {
            return idButton.getItem();
        }
        return null;
    }
}
