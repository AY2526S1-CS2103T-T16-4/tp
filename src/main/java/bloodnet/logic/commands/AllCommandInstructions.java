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
        TEXT.put(AddCommand.COMMAND_WORD, new CommandInformation(AddCommand.DESCRIPTION,
                AddCommand.PARAMETERS, AddCommand.EXAMPLE));

        TEXT.put(EditCommand.COMMAND_WORD, new CommandInformation(EditCommand.DESCRIPTION, EditCommand.PARAMETERS,
                EditCommand.EXAMPLE));

        TEXT.put(DeleteCommand.COMMAND_WORD, new CommandInformation(DeleteCommand.DESCRIPTION, DeleteCommand.PARAMETERS,
                DeleteCommand.EXAMPLE));

        TEXT.put(FindCommand.COMMAND_WORD, new CommandInformation(FindCommand.DESCRIPTION,
                FindCommand.PARAMETERS, FindCommand.EXAMPLE));

        TEXT.put(AddDonationCommand.COMMAND_WORD, new CommandInformation(AddDonationCommand.DESCRIPTION,
                AddDonationCommand.PARAMETERS, AddDonationCommand.EXAMPLE));

        TEXT.put(EditDonationCommand.COMMAND_WORD, new CommandInformation(EditDonationCommand.DESCRIPTION,
                EditDonationCommand.PARAMETERS, EditDonationCommand.EXAMPLE));

        TEXT.put(DeleteDonationCommand.COMMAND_WORD, new CommandInformation(DeleteDonationCommand.DESCRIPTION,
                DeleteDonationCommand.PARAMETERS, DeleteDonationCommand.EXAMPLE));

        TEXT.put(FindDonationsCommand.COMMAND_WORD, new CommandInformation(FindDonationsCommand.DESCRIPTION,
                FindDonationsCommand.PARAMETERS, FindDonationsCommand.EXAMPLE));

        TEXT.put(FindEligibleCommand.COMMAND_WORD, new CommandInformation(FindEligibleCommand.DESCRIPTION,
                FindEligibleCommand.PARAMETERS, FindEligibleCommand.EXAMPLE));

        TEXT.put(HelpCommand.COMMAND_WORD, new CommandInformation(HelpCommand.DESCRIPTION,
                "", ""));

        TEXT.put(ListCommand.COMMAND_WORD, new CommandInformation("Lists all blood donors.",
                "", ""));

        TEXT.put(ClearCommand.COMMAND_WORD, new CommandInformation("Clears the entire list "
                + "of blood donors.", "", ""));

        TEXT.put(ExitCommand.COMMAND_WORD, new CommandInformation("Exits out of the program and closes "
                + "the graphical user interface.", "", ""));
    }
}

