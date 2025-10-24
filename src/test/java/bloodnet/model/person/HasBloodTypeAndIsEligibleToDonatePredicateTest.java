package bloodnet.model.person;

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
import bloodnet.testutil.PersonBuilder;

public class HasBloodTypeAndIsEligibleToDonatePredicateTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        String[] secondArrayOfBloodTypes = new String[]{"O+"};
        HasBloodTypePredicate firstBloodTypePredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligibleToDonatePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypePredicate secondBloodTypePredicate =
                new HasBloodTypePredicate(Arrays.asList(secondArrayOfBloodTypes));

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate firstPredicateCopy =
                new HasBloodTypeAndIsEligibleToDonatePredicate(firstBloodTypePredicate, eligibleToDonatePredicate);

        HasBloodTypeAndIsEligibleToDonatePredicate secondPredicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(secondBloodTypePredicate, eligibleToDonatePredicate);

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
    public void test_personSuccessWithBothPredicates_returnsTrue() {
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        assertTrue(bothPredicates.test(new PersonBuilder().withBloodType("O+").withDateOfBirth("01-01-2005").build()));
    }

    @Test
    public void test_personFailureWithFailingBloodTypePredicate_returnsFalse() {
        HasBloodTypePredicate predicate = new HasBloodTypePredicate(Arrays.asList("O+"));
        IsEligibleToDonatePredicate datePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypeAndIsEligibleToDonatePredicate bothPredicates =
                new HasBloodTypeAndIsEligibleToDonatePredicate(predicate, datePredicate);
        assertFalse(bothPredicates.test(new PersonBuilder().withBloodType("A+").withDateOfBirth("01-01-2005").build()));
    }

    @Test
    public void toStringMethod() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        HasBloodTypePredicate bloodPredicate = new HasBloodTypePredicate(Arrays.asList(arrayOfBloodTypes));
        IsEligibleToDonatePredicate eligiblePredicate = new IsEligibleToDonatePredicate(model);
        HasBloodTypeAndIsEligibleToDonatePredicate predicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(bloodPredicate, new IsEligibleToDonatePredicate(model));
        String expected = HasBloodTypeAndIsEligibleToDonatePredicate.class.getCanonicalName()
                + "{hasBloodType=" + bloodPredicate.toString()
                + ", dateOfBirthAndDonationDate=" + eligiblePredicate.toString() + "}";
        assertEquals(expected, predicate.toString());
    }
}
