package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

public class FindEligibleCommandParserTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    private FindEligibleCommandParser parser = new FindEligibleCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindEligibleCommand.MESSAGE_USAGE));
    }


}
