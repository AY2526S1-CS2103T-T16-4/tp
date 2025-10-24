package bloodnet.logic.commands;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindEligibleCommandTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {

        FindEligibleCommand findFirstCommand = new FindEligibleCommand(Collections.singletonList("O+"));
        FindEligibleCommand findSecondCommand = new FindEligibleCommand(Collections.singletonList("A+"));

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

}

