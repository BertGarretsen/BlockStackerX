package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.conversation.stacker.IdentifierConversation;
import me.epicgodmc.blockstackerx.conversation.stacker.OffsetConversation;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class StackerEditMenu extends Menu {

    /*buttons


    hologram offset
    value format
    max storage
    team stacking
    available blocks
    used

     */


    private final Button idButton;
    private final Button offsetButton;

    public StackerEditMenu(StackerSettings draft)
    {

        setTitle("&c&lEditing Stacker &7(("+draft.getIdentifier()+"))");
        setSize(9*4);

        //temp
        setSlotNumbersVisible();
        //

        idButton = new ButtonConversation(new IdentifierConversation(draft), ItemCreator.of(CompMaterial.INK_SAC,
                "&c&lIdentifier",
                "",
                "&cExample: &7\"sponge_stacker\"",
                "",
                "&cValue: &7\""+draft.getIdentifier()+"\"",
                "&7((Click to edit))"));
        offsetButton = new ButtonConversation(new OffsetConversation(draft), ItemCreator.of(CompMaterial.COMPASS,
                "&c&lOffset",
                "",
                "&cExample: &7\"0.5,1.7,0.5\"",
                "",
                "&cValues:",
                "   &c&lX: &7"+draft.getHologramOffset().getX(),
                "   &c&lY: &7"+draft.getHologramOffset().getY(),
                "   &c&lZ: &7"+draft.getHologramOffset().getZ(),
                "&7((Click to edit))"));
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10) return idButton.getItem();
        if (slot == 11) return offsetButton.getItem();
        return null;
    }

}
