package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.SessionData;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

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
        context.setSessionData(SessionData.IDENTIFIER, input);
        return END_OF_CONVERSATION;
    }
}
