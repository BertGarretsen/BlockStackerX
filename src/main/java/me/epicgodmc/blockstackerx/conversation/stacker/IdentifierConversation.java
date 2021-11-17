package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.StackerConversation;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

public class IdentifierConversation extends StackerConversation {
    private StackerSettings stackerSettings;


    public IdentifierConversation(StackerSettings stackerSettings) {
        this.stackerSettings = stackerSettings;
    }


    @Override
    protected Prompt getFirstPrompt() {
        return new IdentifierPrompt();
    }

    public class IdentifierPrompt extends SimplePrompt {


        @Override
        protected String getPrompt(final ConversationContext context) {
            return "Please enter a stacker identifier, e.g. \"example\"";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return !StackerRegister.getInstance().settingsExist(input);
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return String.format("Identifier \"%s\" is already in use!", invalidInput);
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            StackerSettings clone = stackerSettings.setIdentifier(input);
            openInv(clone, getPlayer(context));
            return END_OF_CONVERSATION;
        }


    }
}
