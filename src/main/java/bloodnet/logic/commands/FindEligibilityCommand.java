package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.person.HasBloodTypePredicate;

/**
 * Finds and lists all persons in BloodNet whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindEligibleCommand extends Command {

    public static final String COMMAND_WORD = "findeligible";
    private final BloodNet bloodNet;


    // This message will definitely be altered.
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all people who are eligible to donate "
            + "given the provided blood type. Eligible first-time blood donors must be between 16 and 60 (1 day before" +
            "their 61st birthday. "
            + "Parameters: KEYWORD [BLOOD_TYPE]...\n"
            + "Example: " + COMMAND_WORD + " O+";

    private final List<String> enteredBloodType;

    public FindEligibleCommand(List<String> enteredBloodType, ReadOnlyBloodNet bloodNet) {
        this.enteredBloodType = enteredBloodType;
        this.bloodNet = new BloodNet(bloodNet);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new HasBloodTypePredicate(enteredBloodType,
                this.bloodNet.getDonationRecordList()));
        int filteredPersonListSize = model.getFilteredPersonList().size();
        return new CommandResult(
                String.format(Messages.MESSAGE_PEOPLE_LISTED_OVERVIEW,
                        filteredPersonListSize,
                        filteredPersonListSize > 1 ? "s" : ""));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindEligibleCommand otherFindCommand = (FindEligibleCommand) other;
        return enteredBloodType.equals(otherFindCommand.enteredBloodType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("bloodType", enteredBloodType)
                .toString();
    }
}
