package bloodnet.model.person;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Represents a Person's birthdate (DD-MM-YYYY) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    public static final String MESSAGE_CONSTRAINTS =
            "The birthdate should be of the format DD-MM-YYYY. "
                    + "Please note that blood donors should be at least 16 years and at "
                    + "most 60 years old (one day before their 61st birthday).";
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
            return !date.isAfter(LocalDate.now().minusYears(16))
                    && !date.isBefore(LocalDate.now().minusYears(61)
                    .plusDays(1));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.format(DATE_FORMATTER);
    }

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

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
