package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AllCommandInstructionsTest {

    @Test
    public void test_addInstructions_success() {
        AllCommandInstructions all = new AllCommandInstructions();
        all.addInstructions();
        assertTrue(AllCommandInstructions.TEXT.containsKey(AddCommand.COMMAND_WORD));
        assertTrue(AllCommandInstructions.TEXT.containsKey(HelpCommand.COMMAND_WORD));
        assertTrue(AllCommandInstructions.TEXT.containsKey(EditCommand.COMMAND_WORD));
    }

    @Test
    public void test_addInstructionsDecriptionsCorrect_success() {
        AllCommandInstructions all = new AllCommandInstructions();
        all.addInstructions();
        assertEquals(AddCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get(AddCommand.COMMAND_WORD).getDescription());
        assertEquals(EditCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get(EditCommand.COMMAND_WORD).getDescription());
        assertEquals(DeleteDonationCommand.DESCRIPTION,
                AllCommandInstructions.TEXT.get(DeleteDonationCommand.COMMAND_WORD).getDescription());
    }
}
