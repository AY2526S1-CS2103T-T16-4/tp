package bloodnet.model.person;

import java.util.List;
import java.util.function.Predicate;

import bloodnet.commons.util.StringUtil;
import bloodnet.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class MatchingBloodType implements Predicate<Person> {
    private final List<String> bloodType;

    public MatchingBloodType(List<String> bloodtype) {
        this.bloodType = bloodtype;
    }

    public boolean test(Person person) {
        return bloodType.stream()
                .anyMatch(bloodType -> bloodType.equalsIgnoreCase(person.getBloodType().value));
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
