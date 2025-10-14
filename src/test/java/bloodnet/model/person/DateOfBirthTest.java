package bloodnet.model.person;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DateOfBirthTest {

    /**
     * Checking what happens if null is thrown
     */
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateOfBirth(null));
    }

    /**
     * The parser does not accept invalid dates.
     */
    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidDateOfBirth = " ";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidDateOfBirth));
    }

    @Test
    public void isValidDateOfBirth() {
        // null name
        assertThrows(NullPointerException.class, () -> DateOfBirth.isValidDateOfBirth(null));

        // invalid name
        assertFalse(DateOfBirth.isValidDateOfBirth("")); // empty string
        assertFalse(DateOfBirth.isValidDateOfBirth(" ")); // spaces only, not accepted
        assertFalse(DateOfBirth.isValidDateOfBirth("\n\n\t")); // only non-alphanumeric characters
        assertFalse(DateOfBirth.isValidDateOfBirth("14-10-1964")); // the earliest day not accepted
        assertFalse(DateOfBirth.isValidDateOfBirth("15-10-2009")); // latest day not accepted
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "XX-DD-YY11")); // contains alphanumeric characters and with the date range
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "31-02-2010")); // contains an invalid day (Februrary 31)
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "33-01-2010")); // contains an invalid date
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "30-13-2010")); // contains an invalid month
        assertFalse(DateOfBirth.isValidDateOfBirth(
                "30-01-1900")); // contains an invalid year

        // valid date of births that are accepted
        assertTrue(DateOfBirth.isValidDateOfBirth("15-10-1964")); // the first day accepted
        assertTrue(DateOfBirth.isValidDateOfBirth("14-10-2009")); // the youngest birthday accepted
        assertTrue(DateOfBirth.isValidDateOfBirth("12-12-2002")); // random birthdate
        assertTrue(DateOfBirth.isValidDateOfBirth("01-07-2003")); // random birthdate

    }

    @Test
    public void equals() {
        DateOfBirth dateOfBirth = new DateOfBirth("01-01-2008");
        DateOfBirth anotherValidBirthDate = new DateOfBirth("14-02-2000");

        // same values -> returns true
        assertTrue(dateOfBirth.equals(new DateOfBirth("01-01-2008")));

        // same object -> returns true
        assertTrue(dateOfBirth.equals(dateOfBirth));

        // null -> returns false
        assertFalse(dateOfBirth.equals(null));

        // different types -> returns false
        assertFalse(dateOfBirth.equals(3.0f));

        // different values -> returns false
        assertFalse(dateOfBirth.equals(anotherValidBirthDate));

        // checking the hashCode method against each other
        assertNotEquals(dateOfBirth.hashCode(), anotherValidBirthDate.hashCode());

        //valid with itself
        assertNotEquals(dateOfBirth.hashCode(), dateOfBirth.hashCode());

    }
}
