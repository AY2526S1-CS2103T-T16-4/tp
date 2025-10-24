package bloodnet.model.person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;


/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class IsEligibleToDonatePredicate implements Predicate<Person> {

    private Model model;
    /**
     * Constructs a {@code HasBloodTypePredicate}.
     */
    public IsEligibleToDonatePredicate(Model model) {
        this.model = model;

    }

    /**
     * This is the predicate that does the filtering. Basically, the user will
     * provide whatever blood types that they would like. Then, filtering will be done
     * both on date of birth and blood type in order to note down eligibility
     * @param person the input argument
     */
    public boolean test(Person person) {
        DateOfBirth dateOfBirth = person.getDateOfBirth();
        Optional<DonationDate> lastDonationDate = model.getFilteredDonationRecordList().stream()
                .filter(donationRecord -> donationRecord.getPersonId()
                        .equals(person.getId()))
                .max(Comparator.comparing(donationRecord -> donationRecord.getDonationDate().getDonationDate()))
                .map(DonationRecord::getDonationDate);

        LocalDate currentDate = LocalDate.now();
        LocalDate earliestDate = currentDate.minusYears(16);
        LocalDate oldestFirstTimeBloodDonors = currentDate.minusYears(61);
        LocalDate oldestRepeatDonor = currentDate.minusYears(66);

        if (!dateOfBirth.getDateOfBirth().isAfter(earliestDate)) {
            if (!dateOfBirth.getDateOfBirth().isAfter(oldestRepeatDonor)
                    && lastDonationDate.isPresent() && lastDonationDate.get().getDonationDate()
                    .plusYears(3).isBefore(currentDate)) {
                return false;
            }
            if (!dateOfBirth.getDateOfBirth().isAfter(oldestFirstTimeBloodDonors) && !lastDonationDate.isPresent()) {
                return false;
            }
            if (lastDonationDate.isPresent() && lastDonationDate.get().getDonationDate()
                    .plusDays(84).isAfter(currentDate)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof IsEligibleToDonatePredicate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
