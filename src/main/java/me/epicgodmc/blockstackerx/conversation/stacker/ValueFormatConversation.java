package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.StackerConversation;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;

public class ValueFormatConversation extends StackerConversation {

    private StackerSettings stackerSettings;

    public ValueFormatConversation(StackerSettings stackerSettings) {
        this.stackerSettings = stackerSettings;
    }

    @Override
    protected Prompt getFirstPrompt() {
        return new ValueFormatPrompt();
    }

    public class ValueFormatPrompt extends SimplePrompt {

        @Override
        protected String getPrompt(ConversationContext context) {
            return "Please enter a value format, e.g. \"Value: {value}\"";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return input.contains("{value}");
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "Format must contain \"{value}\"";
        }

        @Override
        public void onConversationEnd(SimpleConversation conversation, ConversationAbandonedEvent event) {
            System.out.println(event.gracefulExit());
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            stackerSettings.setValueFormat(input);
            openInv(stackerSettings, getPlayer(context));
            return END_OF_CONVERSATION;
        }
    }

}
