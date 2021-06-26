package me.epicgodmc.blockstackerx.conversation.stacker;

import me.epicgodmc.blockstackerx.conversation.StackerConversation;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

public class TeamStackingConversation extends StackerConversation
{

    private StackerSettings stackerSettings;

    public TeamStackingConversation(StackerSettings stackerSettings) {
        this.stackerSettings = stackerSettings;
    }

    @Override
    protected Prompt getFirstPrompt() {
        return new TeamStackingPrompt();
    }

    public class TeamStackingPrompt extends SimplePrompt {

        @Override
        protected String getPrompt(ConversationContext context) {
            return "Please enter wether or not team members will be able to stack, e.g. \"true\" or  \"false\"";
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "please enter either \"true\" or \"false\"";
        }

        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            boolean value = Boolean.parseBoolean(input);
            stackerSettings.setTeamStacking(value);
            openInv(stackerSettings, getPlayer(context));
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
