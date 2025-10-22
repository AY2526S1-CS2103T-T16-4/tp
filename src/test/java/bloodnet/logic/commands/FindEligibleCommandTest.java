package bloodnet.logic.commands;

import static bloodnet.logic.Messages.MESSAGE_PEOPLE_LISTED_OVERVIEW;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalPersons.CARL;
import static bloodnet.testutil.TypicalPersons.ELLE;
import static bloodnet.testutil.TypicalPersons.FIONA;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.person.MatchingBloodType;
import bloodnet.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindEligibleCommandTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {

        FindEligibilityCommand findFirstCommand = new FindEligibilityCommand(Collections.singletonList("O+"));
        FindEligibilityCommand findSecondCommand = new FindEligibilityCommand(Collections.singletonList("A+"));

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

