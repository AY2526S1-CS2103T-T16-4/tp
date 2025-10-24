package bloodnet.model.person;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static java.time.format.ResolverStyle.STRICT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.testutil.PersonBuilder;

public class IsEligibleToDonatePredicateTest {
    Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());


    @Test
    public void equals() {
        IsEligibleToDonatePredicate predicateOne = new IsEligibleToDonatePredicate(model);
        IsEligibleToDonatePredicate predicateTwo = new IsEligibleToDonatePredicate(model);

        // same object -> return true
        assertTrue(predicateOne.equals(predicateOne));

        // compare object with null -> return false
        assertFalse(predicateOne.equals(null));

        // compare object with a different type -> return false
        assertFalse(predicateOne.equals(1));

        // compare two objects with one another
        assertTrue(predicateOne.equals(predicateTwo));
    }


    @Test
    public void donorIsSixteen_returnsTrue() {
        model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        // test youngest age to donate
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String youngestDateOfBirth = LocalDate.now().minusYears(16).format(formatter);
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth(youngestDateOfBirth).build()));
    }

    @Test
    public void donorIsBetweenSixteenAndSixty_returnsTrue() {
        model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        // test when user is in between the age range
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String dateOfBirthInRange = LocalDate.now().minusYears(35).format(formatter);
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth(dateOfBirthInRange).build()));
    }

    @Test
    public void donorIsInRangeAndHasDonatedBefore_returnsTrue() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        String dateOfBirthInRangeAndDonated = LocalDate.now().minusYears(35).format(formatter);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        Person tester = new PersonBuilder().withDateOfBirth(dateOfBirthInRangeAndDonated).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                tester.getId(), new DonationDate("01-01-2020"), new BloodVolume("500")));
        assertTrue(predicate.test(tester));
    }

    @Test
    public void donorIsSixtyYearsOld_returnsTrue() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String oldestFirstTimeDonator = LocalDate.now().minusYears(61)
                .plusDays(1).format(formatter);
        Person oldestFirstTimePerson = new PersonBuilder().withDateOfBirth(oldestFirstTimeDonator).build();
        assertTrue(predicate.test(oldestFirstTimePerson));
    }

    @Test
    public void donorIsSixtyOneButDonatedBefore_returnsTrue() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String olderButHasDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withDateOfBirth(olderButHasDonatedBefore).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                sixtyOneYearsOldPerson.getId(), new DonationDate("01-01-2020"), new BloodVolume("500")));
        assertTrue(predicate.test(sixtyOneYearsOldPerson));
    }

    @Test
    public void oldestRepeatDonor_returnsTrue() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String olderButHasDonatedBefore = LocalDate.now().minusYears(66).plusDays(1)
                .format(formatter);
        Person oldestRepeatDonor = new PersonBuilder().withDateOfBirth(olderButHasDonatedBefore).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                oldestRepeatDonor.getId(), new DonationDate("01-01-2020"), new BloodVolume("500")));
        assertTrue(predicate.test(oldestRepeatDonor));
    }

    @Test
    public void donatedWithLastThreeYears_returnsTrue() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String olderButHasDonatedBefore = LocalDate.now().minusYears(68).format(formatter);
        Person oldestRepeatDonor = new PersonBuilder().withDateOfBirth(olderButHasDonatedBefore).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                oldestRepeatDonor.getId(), new DonationDate("01-01-2025"), new BloodVolume("500")));
        assertTrue(predicate.test(oldestRepeatDonor));
    }

    @Test
    public void donorIsFifteen_returnsFalse() {
        model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String fifteenYearsOld = LocalDate.now().minusYears(16)
                .plusDays(1).format(formatter);
        assertFalse(predicate.test(new PersonBuilder().withDateOfBirth(fifteenYearsOld).build()));
    }

    @Test
    public void validDateOfBirthRangeButDonationLengthTooShort_returnsFalse() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        String dateOfBirthInRangeAndDonated = LocalDate.now().minusYears(35).format(formatter);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        Person tester = new PersonBuilder().withDateOfBirth(dateOfBirthInRangeAndDonated).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                tester.getId(), new DonationDate(LocalDate.now().minusDays(1).format(formatter)),
                new BloodVolume("500")));
        assertFalse(predicate.test(tester));
    }

    @Test
    public void donorIsSixtyOneAndNeverDonatedBefore_returnsFalse() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String neverDonatedBefore = LocalDate.now().minusYears(61).format(formatter);
        Person sixtyOneYearsOldPerson = new PersonBuilder().withDateOfBirth(neverDonatedBefore).build();
        assertFalse(predicate.test(sixtyOneYearsOldPerson));
    }

    @Test
    public void donorIsAboveSixtySixAndDonationIsNotInRange_returnsFalse() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(STRICT);
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String tooLongAgo = LocalDate.now().minusYears(66).format(formatter);
        Person sixtySixYearOld = new PersonBuilder().withDateOfBirth(tooLongAgo).build();
        model.addDonationRecord(new DonationRecord(UUID.randomUUID(),
                sixtySixYearOld.getId(), new DonationDate("01-01-2000"),
                new BloodVolume("500")));
        assertFalse(predicate.test(sixtySixYearOld));
    }

    @Test
    public void toStringMethod() {
        IsEligibleToDonatePredicate predicate = new IsEligibleToDonatePredicate(model);
        String expected = IsEligibleToDonatePredicate.class.getCanonicalName() + "{}";
        assertEquals(expected, predicate.toString());
    }
}
