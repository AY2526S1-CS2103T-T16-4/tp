package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.FindCommand;
import bloodnet.logic.commands.FindEligibilityCommand;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.person.MatchingBloodType;
import bloodnet.model.person.NameContainsKeywordsPredicate;

public class FindEligibilityCommandParserTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    private FindEligibilityCommandParser parser = new FindEligibilityCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindEligibilityCommand.MESSAGE_USAGE));
    }


}
