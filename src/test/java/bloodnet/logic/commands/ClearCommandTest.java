package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalPersonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bloodnet.model.PersonList;
import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyPersonList_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyPersonList_success() {
        Model model = new ModelManager(getTypicalPersonList(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalPersonList(), new UserPrefs());
        expectedModel.setPersonList(new PersonList());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void createSession_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> (new ClearCommand()).createSession(null));
    }

    @Test
    public void createSession_validModel_returnsConfirmationCommandSession() throws CommandException {
        Model model = new ModelManager(getTypicalPersonList(), new UserPrefs());
        CommandSession session = (new ClearCommand()).createSession(model);

        assertTrue(session instanceof ConfirmationCommandSession);
        assertFalse(session.isDone());
    }

}
