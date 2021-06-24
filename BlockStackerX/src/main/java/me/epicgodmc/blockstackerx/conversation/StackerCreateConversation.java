package me.epicgodmc.blockstackerx.conversation;

import me.epicgodmc.blockstackerx.settings.Localization;
import me.epicgodmc.blockstackerx.settings.StackerRegister;
import me.epicgodmc.blockstackerx.settings.StackerSettings;
import me.epicgodmc.blockstackerx.util.Offset;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrefix;
import org.mineacademy.fo.conversation.SimplePrompt;

import java.util.Map;

public class StackerCreateConversation extends SimpleConversation {

    @Override
    protected Prompt getFirstPrompt() {
        return new IdentifierPrompt();
    }

    @Override
    protected ConversationPrefix getPrefix() {
        return new SimplePrefix("&8[&6BlockStackerX&8]&7 ");
    }

    @Override
    protected void onConversationEnd(final ConversationAbandonedEvent event) {

        if (!event.gracefulExit()) {
            tell(event.getContext().getForWhom(), Localization.Conversation.CANCELLED);
        }
    }

    enum Creation {
        IDENTIFIER,
        OFFSET,
        VALUEFORMAT,
        MAXSTORAGE,
        TEAMSTACKING
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
            context.setSessionData(Creation.IDENTIFIER, input);
            return new OffsetPrompt();
        }
    }

    public class OffsetPrompt extends SimplePrompt {

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
            context.setSessionData(Creation.OFFSET, offset);
            return new ValueFormatPrompt();
        }
    }

    public class ValueFormatPrompt extends SimplePrompt {

        @Override
        protected String getPrompt(ConversationContext context) {
            return "Please enter a &6value format&7, e.g. \"Value: {value}\"";
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
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            context.setSessionData(Creation.VALUEFORMAT, input);
            return new MaxStoragePrompt();
        }
    }

    public class MaxStoragePrompt extends SimplePrompt {


        @Override
        protected String getPrompt(ConversationContext context) {
            return "Pleas enter the maximum storage available for this stacker, e.g. \"500\"";
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
            context.setSessionData(Creation.MAXSTORAGE, Integer.parseInt(input));
            return new TeamStackingPrompt();
        }
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
            context.setSessionData(Creation.TEAMSTACKING, Boolean.valueOf(input));


            Map<Object, Object> sessiondata = context.getAllSessionData();
            StackerSettings settings = new StackerSettings((String) sessiondata.get(Creation.IDENTIFIER));
            settings.setItem(StackerRegister.getDEFAULT_ITEM());
            settings.setUsed(StackerRegister.getUSED_DEFAULT_ITEM());

            settings.setHologramOffset((Offset) sessiondata.get(Creation.OFFSET));
            settings.setValueFormat((String) sessiondata.get(Creation.VALUEFORMAT));
            settings.setMaxStorage((Integer) sessiondata.get(Creation.MAXSTORAGE));
            settings.setTeamStacking((Boolean) sessiondata.get(Creation.TEAMSTACKING));

            settings.save();

            tell(context, getPrefix().getPrefix(context) + "new Stacker has been created");
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
