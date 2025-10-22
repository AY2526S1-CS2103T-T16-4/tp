package bloodnet.model.donationrecord;

import static bloodnet.commons.util.AppUtil.checkArgument;
import static java.time.format.ResolverStyle.STRICT;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Represents a Donation Date (DD-MM-YYYY) in PersonList
 * Guarantees: immutable; is valid as declared in {@link #isValidDonationDate(String)}
 */
public class DonationDate {

    public static final String MESSAGE_CONSTRAINTS =
            "The donation date should be of the format DD-MM-YYYY, not in the future, "
                    + "and not more than 130 years ago from today.";
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
    /**
     * This is stored as a LocalDate for easier parsing purposes.
     */
    public final LocalDate value;


    /**
     * Constructs a {@code DonationDate}.
     *
     * @param donationDate A valid donation date.
     */
    public DonationDate(String donationDate) {
        requireNonNull(donationDate);
        checkArgument(isValidDonationDate(donationDate), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(donationDate, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date of birth.
     * Checks to see if it can be parsed as a valid date of birth.
     */
    public static boolean isValidDonationDate(String test) {

        try {
            LocalDate date = LocalDate.parse(test, DATE_FORMATTER);
            LocalDate current = LocalDate.now();
            return !date.isAfter(current)
                    && !date.isBefore(current.minusYears(130));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Formats the date as the same format as inputted ie: DD-MM-YYYY
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
        if (!(other instanceof DonationDate)) {
            return false;
        }

        DonationDate otherDonationDate = (DonationDate) other;
        return value.equals(otherDonationDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

