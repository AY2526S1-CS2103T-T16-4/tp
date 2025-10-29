package bloodnet.logic.commands;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Stores all the message usages in one class.
 */
public class AllCommands {

    public static final CommandInformation[] ALL_COMMANDS = {AddCommand.COMMAND_INFORMATION,
            EditCommand.COMMAND_INFORMATION, DeleteCommand.COMMAND_INFORMATION,
            FindCommand.COMMAND_INFORMATION,  ListCommand.COMMAND_INFORMATION, ClearCommand.COMMAND_INFORMATION,
            AddDonationCommand.COMMAND_INFORMATION, EditDonationCommand.COMMAND_INFORMATION,
            DeleteDonationCommand.COMMAND_INFORMATION, FindDonationsCommand.COMMAND_INFORMATION,
            FindEligibleCommand.COMMAND_INFORMATION,
            HelpCommand.COMMAND_INFORMATION, ExitCommand.COMMAND_INFORMATION};

    public static final HashMap<String, CommandInformation> COMMANDS = new LinkedHashMap<>();

    /**
     * Adds the commands into a HashMap.
     */
    public static void addCommands() {
        for (CommandInformation information: ALL_COMMANDS) {
            COMMANDS.put(information.getCommand(), information);
        }
    }
}
