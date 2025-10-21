package bloodnet.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bloodnet.logic.parser.Prefix;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_DONATION_DISPLAYED_INDEX =
            "The donation record index provided is invalid";
    // Should find a more elegant way to handle plurality.
    // For the second placeholder, pass in '' if singular, 's' if plural
    public static final String MESSAGE_PEOPLE_LISTED_OVERVIEW = "%d person%s listed!";
    // For the second placeholder, pass in '' if singular, 's' if plural
    public static final String MESSAGE_DONATIONS_LISTED_OVERVIEW = "%d donation record%s related to %s found!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Blood Type: ")
                .append(person.getBloodType())
                .append("; Date Of Birth: ")
                .append(person.getDateOfBirth());
        return builder.toString();
    }

    /**
     * Formats the {@code donation record} for display to the user.
     */
    public static String format(DonationRecord record) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Person ID: ").append(record.getPersonId())
                .append("; Donation Date: ").append(record.getDonationDate())
                .append("; Blood Volume: ").append(record.getBloodVolume()).append(" ml");
        return builder.toString();
    }

}
