package me.epicgodmc.blockstackerx.menu;

import me.epicgodmc.blockstackerx.conversation.stacker.AvailableBlocksConversation;
import me.epicgodmc.blockstackerx.conversation.stacker.MaxStorageConversation;
import me.epicgodmc.blockstackerx.conversation.stacker.OffsetConversation;
import me.epicgodmc.blockstackerx.conversation.stacker.ValueFormatConversation;
import me.epicgodmc.blockstackerx.menu.common.DecorationButton;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonRemove;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Collections;

public class StackerEditMenu extends Menu {


    private final Button offsetButton;
    private final Button valueButton;
    private final Button maxStorageButton;
    private final Button teamStackingButton;
    private final Button availableBlocksButton;
    private final Button physicalExample;

    private final Button deleteButton;
    private final Button finishButton;

    public StackerEditMenu(StackerSettings draft) {

        setTitle("&c&lEditing Stacker &8((&7" + draft.getIdentifier() + "&8))");
        setSize(9 * 5);


        offsetButton = new ButtonConversation(new OffsetConversation(draft), ItemCreator.of(CompMaterial.COMPASS,
                "&c&lHologram-Offset",
                "",
                "&cExample: &7\"0.5,1.7,0.5\"",
                "",
                "&cInformation:",
                "&7How far should the hologram be offset",
                "&7From the origin block e.g. the middle of the stacker block?",
                "",
                "&cValues:",
                "   &c&lX: &7" + draft.getHologramOffset().getX(),
                "   &c&lY: &7" + draft.getHologramOffset().getY(),
                "   &c&lZ: &7" + draft.getHologramOffset().getZ(),
                "&7((Click to edit))"));
        valueButton = new ButtonConversation(new ValueFormatConversation(draft), ItemCreator.of(CompMaterial.CHEST,
                "&c&lValue-Format",
                "",
                "&cExample: &7\"Value: {value}\"",
                "",
                "&cInformation:",
                "&7How should the hologram",
                "&7Above the stacker look?",
                "&7((Value will be placed at {value}))",
                "",
                "&cValue: &7\"" + draft.getValueFormat().replace("{value}", "12321") + "&7\"",
                "&7((Click to edit))"));
        maxStorageButton = new ButtonConversation(new MaxStorageConversation(draft), ItemCreator.of(CompMaterial.BARRIER,
                "&c&lMaximum-Storage",
                "",
                "&cExample: &7\"5000\"",
                "",
                "&cInformation:",
                "&7How many blocks should this",
                "&7Stacker be able to store?",
                "&7((set to -1 to disable))",
                "",
                "&cValue: &7" + draft.getMaxStorage(),
                "&7((Click to edit))"));
        teamStackingButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                draft.setTeamStacking(!draft.isTeamStacking());
                redraw();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.ARMOR_STAND,
                        "&c&lTeam-Stacking",
                        "",
                        "&cInformation:",
                        "&7Whether or not every player on",
                        "&7The stacker owner's team can",
                        "&7Interact with the stacker",
                        "",
                        "&cValue: " + draft.isTeamStackingFancy(),
                        "&7((Click to toggle))").build().make();
            }
        };
        availableBlocksButton = new ButtonConversation(new AvailableBlocksConversation(draft), ItemCreator.of(CompMaterial.ANVIL,
                "&c&lAvailable-Blocks",
                "",
                "&cExample: &7\"DIAMOND_BLOCK, GOLD_BLOCK\"",
                "",
                "&cInformation",
                "&7Which block-types should this",
                "&7Stacker be able to stack?",
                "&7Players can only choose one from your list",
                "",
                "&cValues: &7" + Common.join(draft.getAvailableBlockNames(), "&c, &7"),
                "&7((Click to toggle))"));
        physicalExample = new DecorationButton() {
            @Override
            public ItemStack getItem() {
                return draft.getNewStacker();
            }
        };

        finishButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (draft.getState() == StackerSettings.State.ACTIVE)
                {
                    draft.setState(StackerSettings.State.DRAFT);
                }else{
                    draft.setState(StackerSettings.State.ACTIVE);
                }
                redraw();
            }

            @Override
            public ItemStack getItem() {
                if (draft.getState() == StackerSettings.State.ACTIVE) {
                    return ItemCreator.of(CompMaterial.RED_WOOL,
                            "&c&lToggle State",
                            "",
                            "&cInformation",
                            "&7Click this to mark this stacker as a &c&lDraft").build().make();
                } else {
                    return ItemCreator.of(CompMaterial.GREEN_WOOL,
                            "&a&lToggle State",
                            "",
                            "&cInformation",
                            "&7Click this to mark this stacker as &a&lComplete").build().make();
                }
            }
        };
        deleteButton = new ButtonRemove(this, "StackerType", draft.getIdentifier(), (toDelete) ->
        {
            StackerRegister.getInstance().deleteSettings(toDelete);
        });
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot == 10) return physicalExample.getItem();
        if (slot == 11) return offsetButton.getItem();
        if (slot == 12) return valueButton.getItem();
        if (slot == 14) return maxStorageButton.getItem();
        if (slot == 15) return teamStackingButton.getItem();
        if (slot == 16) return availableBlocksButton.getItem();
        if (slot == 36) return deleteButton.getItem();
        if (slot == 40) return finishButton.getItem();
        return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ", Collections.emptyList()).build().make();
    }

}
