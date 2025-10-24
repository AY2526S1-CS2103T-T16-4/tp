package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.testutil.PersonBuilder;
// COME BACK LATER

public class HasBloodTypeAndIsEligibleToDonatePredicateTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        HasBloodTypePredicate firstBloodTypePredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligibleToDonatePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypePredicate secondBloodTypePredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicate
                = new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicateCopy
                = new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate secondPredicate
                = new HasBloodTypeAndIsEligibleToDonatePredicate(secondBloodTypePredicate, eligibleToDonatePredicate);

        // same predicate -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        System.out.println(firstPredicate.toString() + "\n" + firstPredicateCopy.toString());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicateCopy.equals(1));

        // null -> returns false
        assertFalse(firstPredicateCopy.equals(null));

        // different person -> returns false
        assertFalse(firstPredicateCopy.equals(secondPredicate));
    }

    // Still checking for multiple blood types and will be added in v1.5.
    // Currently only check for filtering one blood type. I am pretty sure it works
    // for a list of blood types, but will add tests in next iteration.
    @Test
    public void test_personHasBloodType_returnsTrue() {
        // One keyword
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Collections.singletonList("O+"));
        assertTrue(predicate.test(new PersonBuilder().withBloodType("O+").build()));

    }

    @Test
    public void test_personDoesNotHaveBloodType_returnsFalse() {
        // Zero blood types listed
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withBloodType("O+").build()));

        // Non-matching blood types
        predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        assertFalse(predicate.test(new PersonBuilder().withBloodType("A+").build()));

        //individual too young
        //predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        //assertFalse(predicate.test(new PersonBuilder().withBloodType("O+").withDateOfBirth(
        //      "08-08-2012").build()));

        // individual too old and never donated before
        //individual too young
        //predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        //assertFalse(predicate.test(new PersonBuilder().withBloodType("O+").withDateOfBirth(
        //          "20-10-1964").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("bloodtype1", "bloodtype2");
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(keywords);
        String expected = HasBloodTypePredicate.class.getCanonicalName() + "{bloodType=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
