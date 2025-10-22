package bloodnet.model.person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class EligibleToDonatePredicate implements Predicate<Person> {
    private final DateOfBirth dateOfBirth;
    private final DonationDate donationLength;


    /**
     * Constructs a {@code HasBloodTypePredicate}.
     *
     * @param dateOfBirth The person's birthday
     * @param donationLength A list of donation records that are in the model.
     */
    public EligibleToDonatePredicate(DateOfBirth dateOfBirth, DonationDate donationLength) {
        this.dateOfBirth = dateOfBirth;
        this.donationLength = donationLength;
    }

    /**
     * This is the predicate that does the filtering. Basically, the user will
     * provide whatever blood types that they would like. Then, filtering will be done
     * both on date of birth and blood type in order to note down eligibility
     * @param person the input argument
     * @return
     */
    public boolean test(Person person) {
        LocalDate currentDate = LocalDate.now();
        LocalDate earliestDate = currentDate.minusYears(16);
        LocalDate oldestFirstTimeBloodDonors = currentDate.minusYears(61).plusDays(1);
        LocalDate oldestRepeatDonor = currentDate.minusYears(66).plusDays(1);

        if (dateOfBirth.value.isAfter(earliestDate)) {
            if (!dateOfBirth.value.isBefore(oldestRepeatDonor) && donationLength <= 1068) {
                return false;
            }
            else if (!dateOfBirth.value.isBefore(oldestFirstTimeBloodDonor) && donationLength > 0) {
                return false;
            }
            else {
                return !lastDate.isBefore(currentDate.minusYears(3));
            }

            }
        }
        if (donationRecords.stream().anyMatch(id -> id.getPersonId().equals(person.getId()))) {
            Optional<DonationRecord> latestDate = donationRecords.stream().max(
                    Comparator.comparing(donationRecord -> donationRecord.getDonationDate().value));
            if (latestDate.isPresent()) {
                LocalDate lastDate = latestDate.get().getDonationDate().value;
                return !lastDate.isAfter(LocalDate.now().minusYears(12));
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
        if (!(other instanceof HasBloodTypePredicate)) {
            return false;
        }

        HasBloodTypePredicate otherHasBloodTypePredicate = (HasBloodTypePredicate) other;
        return bloodType.equals(otherHasBloodTypePredicate.bloodType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("bloodType", bloodType).toString();
    }
}
