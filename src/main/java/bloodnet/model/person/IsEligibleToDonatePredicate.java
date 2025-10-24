package bloodnet.model.person;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class IsEligibleToDonatePredicate implements Predicate<Person> {

    /**
     * Constructs a {@code HasBloodTypePredicate}.
     */
    public IsEligibleToDonatePredicate() {
    }

    /**
     * This is the predicate that does the filtering. Basically, the user will
     * provide whatever blood types that they would like. Then, filtering will be done
     * both on date of birth and blood type in order to note down eligibility
     * @param person the input argument
     */
    public boolean test(Person person) {
        DateOfBirth dateOfBirth = person.getDateOfBirth();
        Period period = Period.between(dateOfBirth.value, LocalDate.now());
        int daysSinceLastDonation = period.getDays();
        LocalDate currentDate = LocalDate.now();
        LocalDate earliestDate = currentDate.minusYears(16).minusDays(1);
        LocalDate oldestFirstTimeBloodDonors = currentDate.minusYears(61);
        LocalDate oldestRepeatDonor = currentDate.minusYears(66);

        if (dateOfBirth.value.isAfter(earliestDate)) {
            if (dateOfBirth.value.isBefore(oldestRepeatDonor) && daysSinceLastDonation > 1068) {
                return false;
            }
            if (dateOfBirth.value.isBefore(oldestFirstTimeBloodDonors) && daysSinceLastDonation <= 0) {
                return false;
            }
            if (daysSinceLastDonation < 84) {
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
