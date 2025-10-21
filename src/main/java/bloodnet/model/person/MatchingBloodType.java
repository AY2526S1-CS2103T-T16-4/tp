package bloodnet.model.person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import bloodnet.commons.util.StringUtil;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class MatchingBloodType implements Predicate<Person> {
    private final List<String> bloodType;
    private final List<DonationRecord> donationRecords;

    public MatchingBloodType(List<String> bloodtype, List<DonationRecord> donationRecords) {
        this.bloodType = bloodtype;
        this.donationRecords = donationRecords;
    }

    public boolean test(Person person) {
        boolean bloodTypeMatching = bloodType.stream()
                .anyMatch(bloodType -> bloodType.equalsIgnoreCase(person.getBloodType().value));

        if (bloodTypeMatching) {
            LocalDate currentDate = LocalDate.now();
            LocalDate earliestDate = currentDate.minusYears(16);
            LocalDate oldestFirstTimeBloodDonors = currentDate.minusYears(61).plusDays(1);
            LocalDate oldestRepeatDonor = currentDate.minusYears(66).plusDays(1);

            if (!person.getDateOfBirth().value.isAfter(earliestDate)) {

                if (!person.getDateOfBirth().value.isBefore(oldestFirstTimeBloodDonors)) {
                    return true;
                }
                else if (!person.getDateOfBirth().value.isBefore(oldestRepeatDonor) &&
                donationRecords.stream().anyMatch(id -> id.getPersonId().equals(person.getId()))) {
                    return true;
                }
                else {
                    Optional<DonationRecord> latestDate = donationRecords.stream().max(
                            Comparator.comparing(donationRecord -> donationRecord.getDonationDate().value));

                    if (latestDate.isPresent()) {
                        LocalDate lastDate = latestDate.get().getDonationDate().value;
                        return lastDate.isBefore(currentDate.minusYears(3));
                    }
                }

            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MatchingBloodType)) {
            return false;
        }

        MatchingBloodType otherMatchingBloodType = (MatchingBloodType) other;
        return bloodType.equals(otherMatchingBloodType.bloodType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("bloodType", bloodType).toString();
    }
}
