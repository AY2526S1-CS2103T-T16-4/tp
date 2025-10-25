package bloodnet.model.person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;


/**
 * Tests that a {@code Person} meets eligibility criteria based on {@code dateOfBirth}
 * and days since last donation, if they have donated in the past.
 */
public class IsEligibleToDonatePredicate implements Predicate<Person> {

    private final Model model;
    private final DonationDate donationDate;
    /**
     * Constructs a {@code IsEligibleToDonatePredicate}.
     * Sets {@code donationDate} to today's date.
     */
    public IsEligibleToDonatePredicate(Model model) {
        this.model = model;

        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedTodayDate = todayDate.format(formatter);
        this.donationDate = new DonationDate(formattedTodayDate);
    }

    /**
     * Overloaded constructor.
     * Sets {@code donationDate} to the specified {@code donationDate}.
     */
    public IsEligibleToDonatePredicate(Model model, DonationDate donationDate) {
        this.model = model;
        this.donationDate = donationDate;
    }

    /**
     * Returns true if the person's date of birth and days since last donation fits the criteria.
     * {@code dateOfBirth} is provided by the user.
     *
     * <p>Criteria for invalid donation record:</p>
     * <ul>
     *     <li>Age of person, at the date of donationDate, is < 16</li>
     *
     *     <li>Number of days between the predecessor donation record (if it exists) and the
     *         donationDate is strictly less than 84. That is, if the predecessor donation
     *         record is the 0th date, then return false if donationDate is the 83rd date or lower.</li>
     *
     *     <li>Number of days between the successor donation record (if it exists) and the
     *         donationDate is strictly less than 84. That is, if the donationDate is the
     *         0th date, then return false if successor donation record is the 83rd date or lower.</li>
     *
     *     <li>If donor is a first-time donor (ie, there exists no predecessor donation
     *         record for that donor) AND donationDate >= 61st birthdate of donor.</li>
     *
     *     <li>If donor is not a first-time donor (ie, there exists some predecessor donation
     *         record for that donor) AND donor has not donated in the last 3 years
     *         AND donationDate >= 66th birthdate of donor. </li>
     * </ul>
     *
     * @param person Person you are checking the {@code dateOfBirth} and days since last donation for.
     */
    public boolean test(Person person) {
        LocalDate donationDateValue = donationDate.getValue();
        LocalDate dateOfBirth = person.getDateOfBirth().getValue();

        // 1. Age of person at donationDate must be >= 16
        int ageAtDonation = Period.between(dateOfBirth, donationDateValue).getYears();
        if (ageAtDonation < 16) {
            return false;
        }

        // Find predecessor (last donation before donationDate)
        Optional<DonationDate> predecessorDonationDate = model.getFilteredDonationRecordList().stream()
                .filter(donationRecord -> donationRecord.getPersonId().equals(person.getId()))
                .map(DonationRecord::getDonationDate)
                .filter(dd -> dd.getValue().isBefore(donationDateValue))
                .max(Comparator.comparing(DonationDate::getValue));

        // Find successor (first donation after donationDate)
        Optional<DonationDate> successorDonationDate = model.getFilteredDonationRecordList().stream()
                .filter(donationRecord -> donationRecord.getPersonId().equals(person.getId()))
                .map(DonationRecord::getDonationDate)
                .filter(dd -> dd.getValue().isAfter(donationDateValue))
                .min(Comparator.comparing(DonationDate::getValue));

        // 2. Days between predecessor and donationDate must be >= 84
        if (predecessorDonationDate.isPresent()) {
            long daysSinceLastDonation = ChronoUnit.DAYS.between(predecessorDonationDate.get().getValue(),
                                                                 donationDateValue);
            if (daysSinceLastDonation < 84) {
                return false;
            }
        }

        // 3. Days between donationDate and successor must be >= 84
        if (successorDonationDate.isPresent()) {
            long daysToNextDonation = ChronoUnit.DAYS.between(donationDateValue,
                                                              successorDonationDate.get().getValue());
            if (daysToNextDonation < 84) {
                return false;
            }
        }

        // 4. First-time donor and donationDate >= 61st birthday
        LocalDate sixtyFirstBirthday = dateOfBirth.plusYears(61);
        if (predecessorDonationDate.isEmpty() && !donationDateValue.isBefore(sixtyFirstBirthday)) {
            return false;
        }

        // 5. Not first-time donor AND not donated in last 3 years AND donationDate >= 66th birthday
        LocalDate sixtySixthBirthday = dateOfBirth.plusYears(66);
        if (predecessorDonationDate.isPresent()) {
            LocalDate lastDonation = predecessorDonationDate.get().getValue();
            if (!lastDonation.plusYears(3).isAfter(donationDateValue)
                && !donationDateValue.isBefore(sixtySixthBirthday)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls or if it is of a different type
        return other instanceof IsEligibleToDonatePredicate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
