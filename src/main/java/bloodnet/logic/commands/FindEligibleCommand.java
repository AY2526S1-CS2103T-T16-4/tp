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
 * Finds and lists all eligible person based on blood type, date of birth
 * and number of days since their last donation, if applicable.
 */
public class FindEligibleCommand extends Command {

    public static final String COMMAND_WORD = "findeligible";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all people who are eligible to donate"
            + " blood for the specified blood type(s) (case-insensitive) \nand displays them as a list with index "
            + "numbers. \nAll eligible blood donors must be at least 16 (inclusive) years old.\nThe maximum age for "
            + "first-time donors is generally 60 years and 354 days, but repeat donors may have different age limits \n"
            + "depending on donation history. \n"
            + "Parameters: BLOOD_TYPE (can include multiple)\n"
            + "Example: " + COMMAND_WORD + " O+ A+ B+";


    private final List<String> enteredBloodTypes;

    public FindEligibleCommand(List<String> enteredBloodTypes) {
        this.enteredBloodTypes = enteredBloodTypes;
    }

    @Override
    public InputResponse execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new HasBloodTypeAndIsEligibleToDonatePredicate(
                new HasBloodTypePredicate(enteredBloodTypes), new IsEligibleToDonatePredicate(model)));
        int filteredPersonListSize = model.getFilteredPersonList().size();
        return new InputResponse(
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
        if (!(other instanceof FindEligibleCommand)) {
            return false;
        }

        FindEligibleCommand otherFindCommand = (FindEligibleCommand) other;
        return enteredBloodTypes.equals(otherFindCommand.enteredBloodTypes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("bloodTypes", enteredBloodTypes)
                .toString();
    }
}
