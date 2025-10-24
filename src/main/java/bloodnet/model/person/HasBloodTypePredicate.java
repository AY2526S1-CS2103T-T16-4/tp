package bloodnet.model.person;

import java.util.List;
import java.util.function.Predicate;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code BloodType} matches any of the people's blood types.
 */
public class HasBloodTypePredicate implements Predicate<Person> {
    private final List<String> bloodType;

    /**
     * Constructs a {@code HasBloodTypePredicate}.
     *
     * @param bloodType A list of blood types to be matched.
     */
    public HasBloodTypePredicate(List<String> bloodType) {
        this.bloodType = bloodType;
    }

    // Very well aware that it is not the most elegant thing.
    /**
     * This is the predicate that does the filtering. Basically, the user will
     * provide whatever blood types that they would like. Then, filtering will be done
     * both on date of birth and blood type in order to note down eligibility
     * @param person the input argument
     * @return
     */
    public boolean test(Person person) {
        boolean bloodTypeMatching = bloodType.stream()
                .anyMatch(bloodType -> bloodType.equalsIgnoreCase(person.getBloodType().value));
        return bloodTypeMatching;
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
