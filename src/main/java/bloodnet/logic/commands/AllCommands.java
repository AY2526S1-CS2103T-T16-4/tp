package bloodnet.logic.commands;

import java.util.Map;

/**
 * Stores all the message usages in one class.
 */
public class AllCommands {

    public static final Map<String, CommandInformation> COMMANDS = Map.ofEntries(
            Map.entry(AddCommand.COMMAND_WORD, AddCommand.COMMAND_INFORMATION),
            Map.entry(AddDonationCommand.COMMAND_WORD, AddDonationCommand.COMMAND_INFORMATION),
            Map.entry(ClearCommand.COMMAND_WORD, ClearCommand.COMMAND_INFORMATION),
            Map.entry(DeleteCommand.COMMAND_WORD, DeleteCommand.COMMAND_INFORMATION),
            Map.entry(DeleteDonationCommand.COMMAND_WORD, DeleteDonationCommand.COMMAND_INFORMATION),
            Map.entry(EditCommand.COMMAND_WORD, EditCommand.COMMAND_INFORMATION),
            Map.entry(EditDonationCommand.COMMAND_WORD, EditDonationCommand.COMMAND_INFORMATION),
            Map.entry(ExitCommand.COMMAND_WORD, ExitCommand.COMMAND_INFORMATION),
            Map.entry(FindCommand.COMMAND_WORD, FindCommand.COMMAND_INFORMATION),
            Map.entry(FindDonationsCommand.COMMAND_WORD, FindDonationsCommand.COMMAND_INFORMATION),
            Map.entry(FindEligibleCommand.COMMAND_WORD, FindEligibleCommand.COMMAND_INFORMATION),
            Map.entry(HelpCommand.COMMAND_WORD, HelpCommand.COMMAND_INFORMATION),
            Map.entry(ListCommand.COMMAND_WORD, ListCommand.COMMAND_INFORMATION));
}
