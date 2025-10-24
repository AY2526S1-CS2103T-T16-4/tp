package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.DeleteCommand;
import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.person.BloodType;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindEligibleCommandParser implements Parser<FindEligibleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEligibleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] statedBloodType = trimmedArgs.split("\\s+");
        List<String> list = Arrays.asList(statedBloodType);

        for (String l : list) {
            BloodType bloodType = ParserUtil.parseBloodType(l);
        }
        return new FindEligibleCommand(list);
    }
}
