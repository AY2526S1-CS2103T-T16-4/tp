package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindEligibilityCommand;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.person.MatchingBloodType;
import bloodnet.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindEligibilityParserCommand implements Parser<FindEligibilityCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEligibilityCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEligibilityCommand.MESSAGE_USAGE));
        }

        String[] statedBloodType = trimmedArgs.split("\\s+");

        return new FindEligibilityCommand(new MatchingBloodType(statedBloodType));
    }

}
