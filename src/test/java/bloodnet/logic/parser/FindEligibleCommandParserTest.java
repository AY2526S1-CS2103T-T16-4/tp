package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.model.person.BloodType;


public class FindEligibleCommandParserTest {

    private final FindEligibleCommandParser parser = new FindEligibleCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // only spaces are included
        assertParseFailure(parser, "    ", BloodType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        // invalid blood type, then valid blood type
        assertParseFailure(parser, "T+ A+", BloodType.MESSAGE_CONSTRAINTS);

        // valid blood type, then invalid blood type
        assertParseFailure(parser, "A+ T+", BloodType.MESSAGE_CONSTRAINTS);

        // valid blood type, then invalid blood type
        assertParseFailure(parser, "1232312321", BloodType.MESSAGE_CONSTRAINTS);

    }

    @Test
    public void parse_validArguments_success() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        FindEligibleCommand findEligible = new FindEligibleCommand(Arrays.asList(arrayOfBloodTypes));
        String userInput = "O+ A+ AB+";
        assertParseSuccess(parser, userInput, findEligible);
    }
}
