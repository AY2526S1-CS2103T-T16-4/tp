package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AllCommandInstructionsTest {

    @Test
    public void test_addInstructions_success() {
        AllCommandInstructions all = new AllCommandInstructions();
        all.addInstructions();
        assertTrue(AllCommandInstructions.TEXT.containsKey("\n" + AddCommand.COMMAND_WORD));
        assertTrue(AllCommandInstructions.TEXT.containsKey("\n" + HelpCommand.COMMAND_WORD));
        assertTrue(AllCommandInstructions.TEXT.containsKey("\n" + EditCommand.COMMAND_WORD));
    }

    @Test
    public void test_addInstructionsDecriptionsCorrect_success() {
        AllCommandInstructions all = new AllCommandInstructions();
        all.addInstructions();
        assertEquals(AddCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get("\n" + AddCommand.COMMAND_WORD).getDescription());
        assertEquals(EditCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get("\n" + EditCommand.COMMAND_WORD).getDescription());
        assertEquals(DeleteDonationCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get("\n" + DeleteDonationCommand.COMMAND_WORD).getDescription());
    }
}
