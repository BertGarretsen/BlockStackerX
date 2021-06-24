package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.SessionData;
import me.epicgodmc.blockstackerx.util.Offset;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

public class OffsetPrompt extends SimplePrompt
{

    @Override
    protected String getPrompt(ConversationContext context) {
        return "Please enter a hologram offset, e.g. \"0.5,1.7,0.5\"";
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        Offset offset = Offset.of(input);
        return offset.isValid();
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return "Please specify in the format displayed here \"0.5,1.7,0.5\"";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        Offset offset = Offset.of(input);
        context.setSessionData(SessionData.OFFSET, offset);
        return END_OF_CONVERSATION;
    }

}
