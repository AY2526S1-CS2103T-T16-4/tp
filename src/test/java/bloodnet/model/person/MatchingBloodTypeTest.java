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

public class MatchingBloodTypeTest {
    Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        List<String> firstBloodType = Collections.singletonList("A+");
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+");

        MatchingBloodType firstPredicate = new MatchingBloodType(firstBloodType, model.getFilteredDonationRecordList());
        MatchingBloodType secondPredicate = new MatchingBloodType(listOfBloodTypes, model.getFilteredDonationRecordList());

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        MatchingBloodType firstPredicateCopy = new MatchingBloodType(firstBloodType, model.getFilteredDonationRecordList());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    // Still checking for multiple blood types and will be added in v1.5.
    // Currently only check for filtering one blood type. I am pretty sure it works
    // for a list of blood types, but will add tests in next iteration.
    @Test
    public void test_personHasBloodType_returnsTrue() {
        // One keyword
        MatchingBloodType predicate = new MatchingBloodType(Collections.singletonList("O+"),
                model.getFilteredDonationRecordList());
        assertTrue(predicate.test(new PersonBuilder().withBloodType("O+").build()));

    }

    @Test
    public void test_personDoesNotHaveBloodType_returnsFalse() {
        // Zero keywords
        MatchingBloodType predicate = new MatchingBloodType(Collections.emptyList(),
                model.getFilteredDonationRecordList());
        assertFalse(predicate.test(new PersonBuilder().withBloodType("O+").build()));

        // Non-matching keyword
        predicate = new MatchingBloodType(Arrays.asList("O+"),
                model.getFilteredDonationRecordList());
        assertFalse(predicate.test(new PersonBuilder().withBloodType("A+").build()));

    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("bloodtype1", "bloodtype2");
        MatchingBloodType predicate = new MatchingBloodType(keywords,
                model.getFilteredDonationRecordList());

        String expected = MatchingBloodType.class.getCanonicalName() + "{bloodType=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
