package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonorIsSamePersonPredicate;
import bloodnet.model.person.Person;

/**
 * Finds and list all donation records in bloodnet related to
 * the person identified by the index number used in the displayed
 * person list from the bloodnet.
 */
public class FindDonationsCommand extends Command {
    public static final String COMMAND_WORD = "finddonations";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all donation records related to "
            + "the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetPersonIndex;

    public FindDonationsCommand(Index targetPersonIndex) {
        this.targetPersonIndex = targetPersonIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToFindRecordsOf = getPersonToFindRecordsOf(model);
        DonorIsSamePersonPredicate predicate = new DonorIsSamePersonPredicate(personToFindRecordsOf);
        model.updateFilteredDonationRecordList(predicate);
        int filteredDonationRecordListSize = model.getFilteredDonationRecordList().size();
        return new CommandResult(
                String.format(Messages.MESSAGE_DONATIONS_LISTED_OVERVIEW,
                        filteredDonationRecordListSize,
                        filteredDonationRecordListSize > 1 ? "s" : "",
                        personToFindRecordsOf.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handle nulls
        if (!(other instanceof FindDonationsCommand)) {
            return false;
        }

        FindDonationsCommand otherFindDonationsCommand = (FindDonationsCommand) other;
        return targetPersonIndex.equals(otherFindDonationsCommand.targetPersonIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .toString();
    }

    private Person getPersonToFindRecordsOf(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(targetPersonIndex.getZeroBased());
    }
}
