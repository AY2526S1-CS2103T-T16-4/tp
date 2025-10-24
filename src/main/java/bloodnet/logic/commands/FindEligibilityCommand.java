package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.model.Model;
import bloodnet.model.person.HasBloodTypeAndIsEligibleToDonatePredicate;
import bloodnet.model.person.HasBloodTypePredicate;
import bloodnet.model.person.IsEligibleToDonatePredicate;

/**
 * Finds and lists all eligible person based on date of birth, blood type
 * and days since last donation.
 */
public class FindEligibleCommand extends Command {

    public static final String COMMAND_WORD = "findeligible";

    // This message will definitely be altered.
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all people who are eligible to donate "
            + "given the provided blood type. \n Eligible first-time blood donors must be between 16 and 60 "
            + "(1 day before their 61st birthday.) \n"
            + "Parameters: KEYWORD [BLOOD_TYPE]...\n"
            + "Example: " + COMMAND_WORD + " O+";

    private final List<String> enteredBloodType;

    public FindEligibleCommand(List<String> enteredBloodType) {
        this.enteredBloodType = enteredBloodType;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new HasBloodTypeAndIsEligibleToDonatePredicate(
                new HasBloodTypePredicate(enteredBloodType), new IsEligibleToDonatePredicate()));
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
