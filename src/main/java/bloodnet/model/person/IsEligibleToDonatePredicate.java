package bloodnet.model.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.BloodVolume;
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
        // Using a dummy blood volume, as we want to reuse DonationRecord's .validate() method.
        // Not the best design.
        DonationRecord donationRecord = new DonationRecord(null,
                                                            person.getId(),
                                                            donationDate,
                                                            new BloodVolume("1"));
        ArrayList<String> validationErrorStrings = donationRecord.validate(model);
        return validationErrorStrings.isEmpty();
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
