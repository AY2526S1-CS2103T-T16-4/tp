package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.MatchingBloodType;

/**
 * Finds and lists all persons in BloodNet whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindEligibilityCommand extends Command {

    public static final String COMMAND_WORD = "findeligible";

    // This message will definitely be altered.
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all people who are eligible to donate "
            + "given the provided blood type. According to the Health Sources Authority, the authority in "
            + "charge of blood donations, states that eligible blood donors must be at least 16 years old. "
            + "The maximum age for first-time blood donors is 1 day before their 61st birthday but users "
            + "who have donated before can donate up to 1 day before their 66th birthday. "
            + "Donors who have donated in the last three years are can continue donating beyond 66 years old. "
            + "Furthermore, blood donors can only donate once every 12 weeks. \n"
            + "Parameters: KEYWORD [BLOOD_TYPE]...\n"
            + "Example: " + COMMAND_WORD + " O+";

    private final List<String> enteredBloodType;

    public FindEligibilityCommand(List<String> enteredBloodType) {
        this.enteredBloodType = enteredBloodType;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new MatchingBloodType(enteredBloodType,
                model.getFilteredDonationRecordList()));
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

        FindEligibilityCommand otherFindCommand = (FindEligibilityCommand) other;
        return enteredBloodType.equals(otherFindCommand.enteredBloodType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("bloodType", enteredBloodType)
                .toString();
    }
}
