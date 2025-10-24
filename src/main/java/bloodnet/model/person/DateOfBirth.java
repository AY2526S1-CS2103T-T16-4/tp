package bloodnet.model.person;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.time.format.ResolverStyle.STRICT;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Represents a Person's birthdate (DD-MM-YYYY) in BloodNet
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    /**
     * Blood donation requiremenets taken from: https://www.hsa.gov.sg/blood-donation/can-i-donate
     */
    public static final String MESSAGE_CONSTRAINTS =
            "The date of birth should be of the format DD-MM-YYYY, not in the future,"
                    + " and not more than 130 years ago from today.";
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
    /**
     * This is stored as a LocalDate for easier parsing purposes.
     */
    public final LocalDate value;


    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDateOfBirth(dateOfBirth), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date of birth.
     * Checks to see if it can be parsed as a valid date of birth.
     */
    public static boolean isValidDateOfBirth(String test) {

        try {
            LocalDate date = LocalDate.parse(test, DATE_FORMATTER);
            LocalDate current = LocalDate.now();
            return !date.isAfter(current)
                    && !date.isBefore(current.minusYears(130));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getDateOfBirth() {
        return value;
    }

    /**
     * Formats the date as the same format as inputted ie: DD-MM-YYY
     */
    @Override
    public String toString() {
        return value.format(DATE_FORMATTER);
    }

    /**
     * Compares two dates of birth with one another.
     *
     * @param other date
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDateOfBirth = (DateOfBirth) other;
        return value.equals(otherDateOfBirth.value);
    }

    /**
     * This returns the same hashCode if the values are equal to one another.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

