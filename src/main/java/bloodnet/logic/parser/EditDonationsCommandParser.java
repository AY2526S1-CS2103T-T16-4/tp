package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static java.util.Objects.requireNonNull;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.EditDonationsCommand;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditDonationsCommandParser implements Parser<EditDonationsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditDonationsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_BLOOD_VOLUME, PREFIX_DONATION_DATE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, EditDonationsCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_BLOOD_VOLUME, PREFIX_DONATION_DATE);

        EditDonationsCommand.EditDonationRecordDescriptor editDonationRecordDescriptor =
                new EditDonationsCommand.EditDonationRecordDescriptor();

        if (argMultimap.getValue(PREFIX_BLOOD_VOLUME).isPresent()) {
            editDonationRecordDescriptor.setBloodVolume(
                    ParserUtil.parseBloodVolume(argMultimap.getValue(PREFIX_BLOOD_VOLUME).get()));
        }

        if (argMultimap.getValue(PREFIX_DONATION_DATE).isPresent()) {
            editDonationRecordDescriptor.setDonationDate(
                    ParserUtil.parseDonationDate(argMultimap.getValue(PREFIX_DONATION_DATE).get()));
        }

        if (!editDonationRecordDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditDonationsCommand.MESSAGE_NOT_EDITED);
        }

        return new EditDonationsCommand(index, editDonationRecordDescriptor);
    }


}
