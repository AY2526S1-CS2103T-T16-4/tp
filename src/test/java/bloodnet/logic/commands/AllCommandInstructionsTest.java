package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AllCommandInstructionsTest {

    @Test
    public void test_addInstructions_success() {
        AllCommands all = new AllCommands();
        assertTrue(AllCommands.COMMANDS.containsKey(AddCommand.COMMAND_WORD));
        assertTrue(AllCommands.COMMANDS.containsKey(HelpCommand.COMMAND_WORD));
        assertTrue(AllCommands.COMMANDS.containsKey(EditCommand.COMMAND_WORD));
    }

    @Test
    public void test_addInstructionsDescriptionsCorrect_success() {
        AllCommands all = new AllCommands();
        assertEquals(AddCommand.COMMAND_INFORMATION.getDescription(),
                AllCommands.COMMANDS.get(AddCommand.COMMAND_WORD).getDescription());
        assertEquals(EditCommand.COMMAND_INFORMATION.getDescription(),
                AllCommands.COMMANDS.get(EditCommand.COMMAND_WORD).getDescription());
        assertEquals(DeleteDonationCommand.COMMAND_INFORMATION.getDescription(),
                AllCommands.COMMANDS.get(DeleteDonationCommand.COMMAND_WORD).getDescription());
    }
}
