package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import bloodnet.model.AddressBook;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
