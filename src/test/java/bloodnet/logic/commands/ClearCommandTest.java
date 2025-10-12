package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;

import org.junit.jupiter.api.Test;

import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyBloodNet_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyBloodNet_success() {
        Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        expectedModel.setBloodNet(new BloodNet());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
