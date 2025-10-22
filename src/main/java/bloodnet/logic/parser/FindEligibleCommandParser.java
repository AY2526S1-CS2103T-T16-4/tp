package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindEligibleCommandParser implements Parser<FindEligibleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEligibleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEligibleCommand.MESSAGE_USAGE));
        }

        String[] statedBloodType = trimmedArgs.split("\\s+");

        return new FindEligibleCommand(Arrays.asList(statedBloodType));
    }

}
