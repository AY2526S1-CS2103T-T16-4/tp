package bloodnet.model.person;

import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class HasBloodTypeAndIsEligibleToDonatePredicate implements Predicate<Person> {
    private final HasBloodTypePredicate bloodTypePredicate;
    private final IsEligibleToDonatePredicate eligibleToDonatePredicate;


    /**
     * Constructs a {@code HasBloodTypePredicate}.
     *
     * @param bloodTypePredicate Predicate as to whether the person has the relevant blood type.
     * @param eligibleToDonatePredicate Predicate as to whether the person is eligible based on date of
     *                                  birth and donation date.
     */
    public HasBloodTypeAndIsEligibleToDonatePredicate(HasBloodTypePredicate bloodTypePredicate,
                                                      IsEligibleToDonatePredicate eligibleToDonatePredicate) {
        this.bloodTypePredicate = bloodTypePredicate;
        this.eligibleToDonatePredicate = eligibleToDonatePredicate;
    }

    /**
     * This function returns true if both of the predicates are true.
     * @param person the input argument
     */
    public boolean test(Person person) {
        return bloodTypePredicate.test(person) && eligibleToDonatePredicate.test(person);
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

        HasBloodTypeAndIsEligibleToDonatePredicate otherHasloodTypeAndIsEligibleToDonatePredicate =
                (HasBloodTypeAndIsEligibleToDonatePredicate) other;
        return this.bloodTypePredicate.equals(otherHasloodTypeAndIsEligibleToDonatePredicate.bloodTypePredicate)
                && this.eligibleToDonatePredicate
                == otherHasloodTypeAndIsEligibleToDonatePredicate.eligibleToDonatePredicate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("hasBloodType", bloodTypePredicate)
                .add("dateOfBirthAndDonationDate", eligibleToDonatePredicate).toString();
    }
}
