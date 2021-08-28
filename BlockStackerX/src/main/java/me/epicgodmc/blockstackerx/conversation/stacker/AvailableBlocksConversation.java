package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.StackerConversation;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.CompMaterial;

public class AvailableBlocksConversation extends StackerConversation {
    private StackerSettings stackerSettings;


    public AvailableBlocksConversation(StackerSettings stackerSettings) {
        this.stackerSettings = stackerSettings;
    }


    @Override
    protected Prompt getFirstPrompt() {
        return new AvailableBlocksPrompt();
    }

    public class AvailableBlocksPrompt extends SimplePrompt {


        @Override
        protected String getPrompt(final ConversationContext context) {
            return "Please enter an array of blocks seperated by ',' e.g. \"DIAMOND_BLOCK,GOLD_BLOCK\"";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            String[] array = input.trim().split(",");

            if (array.length == 0)
            {
                context.setSessionData("FAIL", "Please specify at least one block");
                return false;
            }

            for (String s : array) {
                CompMaterial compMaterial = CompMaterial.fromString(s.trim());
                if (compMaterial == null) {
                    context.setSessionData("FAIL", "\"" + s + "\" could not be found");
                    return false;
                }
            }
            return true;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return (String) context.getSessionData("FAIL");
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            String[] array = input.trim().split(",");
            stackerSettings.setAvailableBlocks(array);
            openInv(stackerSettings, getPlayer(context));
            return END_OF_CONVERSATION;
        }

    }

}
