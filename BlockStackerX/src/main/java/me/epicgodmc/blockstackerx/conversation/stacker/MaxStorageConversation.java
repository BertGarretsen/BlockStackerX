package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.StackerConversation;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;

public class MaxStorageConversation extends StackerConversation
{

    private StackerSettings stackerSettings;

    public MaxStorageConversation(StackerSettings stackerSettings) {
        this.stackerSettings = stackerSettings;
    }

    @Override
    protected Prompt getFirstPrompt() {
        return new MaxStoragePrompt();
    }

    public class MaxStoragePrompt extends SimplePrompt {


        @Override
        protected String getPrompt(ConversationContext context) {
            return "Please enter the maximum storage available for this stacker, e.g. \"500\"";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            if (!Valid.isInteger(input)) return false;

            final int amount = Integer.parseInt(input);
            return amount > 0;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "Please only specify a non-zero number.";
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            if (Valid.isInteger(input))
            {
                stackerSettings.setMaxStorage(Integer.parseInt(input));
            }
            openInv(stackerSettings, getPlayer(context));
            return END_OF_CONVERSATION;
        }
    }
}
