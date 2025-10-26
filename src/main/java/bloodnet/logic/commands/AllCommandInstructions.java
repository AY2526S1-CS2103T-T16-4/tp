package bloodnet.logic.commands;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A class that stores all instructions and their usage in a HashMap.
 */
public class AllCommandInstructions {

    public static final HashMap<String, CommandInformation> TEXT = new LinkedHashMap<>();

    /**
     * Adds the instructions to the HashMap.
     */
    public void addInstructions() {
        TEXT.put("\n"+ AddCommand.COMMAND_WORD, new CommandInformation(AddCommand.DESCRIPTION,
                AddCommand.PARAMETERS, AddCommand.EXAMPLE));

        TEXT.put("\n" + EditCommand.COMMAND_WORD, new CommandInformation(EditCommand.DESCRIPTION, EditCommand.PARAMETERS,
                EditCommand.EXAMPLE));

        TEXT.put("\n" + DeleteCommand.COMMAND_WORD, new CommandInformation(DeleteCommand.DESCRIPTION, DeleteCommand.PARAMETERS,
                DeleteCommand.EXAMPLE));

        TEXT.put("\n" + FindCommand.COMMAND_WORD, new CommandInformation(FindCommand.DESCRIPTION,
                FindCommand.PARAMETERS, FindCommand.EXAMPLE));

        TEXT.put("\n" + AddDonationCommand.COMMAND_WORD, new CommandInformation(AddDonationCommand.DESCRIPTION,
                AddDonationCommand.PARAMETERS, AddDonationCommand.EXAMPLE));

        TEXT.put("\n" + EditDonationCommand.COMMAND_WORD, new CommandInformation(EditDonationCommand.DESCRIPTION,
                EditDonationCommand.PARAMETERS, EditDonationCommand.EXAMPLE));

        TEXT.put("\n" + DeleteDonationCommand.COMMAND_WORD, new CommandInformation(DeleteDonationCommand.DESCRIPTION,
                DeleteDonationCommand.PARAMETERS, DeleteDonationCommand.EXAMPLE));

        TEXT.put("\n" + FindDonationsCommand.COMMAND_WORD, new CommandInformation(FindDonationsCommand.DESCRIPTION,
                FindDonationsCommand.PARAMETERS, FindDonationsCommand.EXAMPLE));

        TEXT.put("\n" + FindEligibleCommand.COMMAND_WORD, new CommandInformation(FindEligibleCommand.DESCRIPTION,
                FindEligibleCommand.PARAMETERS, FindEligibleCommand.EXAMPLE));

        TEXT.put("\n" + HelpCommand.COMMAND_WORD, new CommandInformation(HelpCommand.DESCRIPTION,
                "", ""));

        TEXT.put("\n" + ListCommand.COMMAND_WORD, new CommandInformation("Lists all blood donors.",
                "", ""));

        TEXT.put("\n" + ClearCommand.COMMAND_WORD, new CommandInformation("Clears the entire list "
                + "of blood donors.", "", ""));

        TEXT.put("\n" + ExitCommand.COMMAND_WORD, new CommandInformation("Exits out of the program and closes "
                + "the graphical user interface.", "", ""));
    }
}

