package bloodnet.logic.commands;

import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindEligibleCommand}.
 */
public class FindEligibleCommandTest {
    private final Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+", "A+");
        List<String> secondListOfBloodTypes = Arrays.asList("AB+", "A+");

        FindEligibleCommand findEligibleFirstCommand = new FindEligibleCommand(listOfBloodTypes);
        FindEligibleCommand findEligibleFirstCommandCopy = new FindEligibleCommand(listOfBloodTypes);
        FindEligibleCommand findEligibleSecondCommand = new FindEligibleCommand(secondListOfBloodTypes);

        // same object -> returns true
        assertTrue(findEligibleFirstCommand.equals(findEligibleFirstCommand));

        // different types -> returns false
        assertFalse(findEligibleFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findEligibleFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findEligibleFirstCommand.equals(findEligibleSecondCommand));

        // same values and different object -> returns false
        assertTrue(findEligibleFirstCommand.equals(findEligibleFirstCommandCopy));
    }

    @Test
    public void toStringMethod() {
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+", "A+");
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(listOfBloodTypes);

        String expected = FindEligibleCommand.class.getCanonicalName() + "{bloodTypes=" + listOfBloodTypes + "}";
        assertEquals(expected, findEligibleCommand.toString());
    }


    @Test
    public void execute_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }



}

