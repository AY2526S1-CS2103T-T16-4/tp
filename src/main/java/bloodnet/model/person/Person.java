package bloodnet.model.person;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import bloodnet.commons.util.ToStringBuilder;

/**
 * Represents a Person in the bloodnet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    // Used to autoincrement the id
    static private Integer nextId = 1;

    // Identity fields
    private final Integer id;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final BloodType bloodType;
    private final DateOfBirth dateOfBirth;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, BloodType bloodType,
                  DateOfBirth dateOfBirth) {
        requireAllNonNull(name, phone, email, bloodType);
        this.id = nextId;
        nextId++;

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getId() {return id; }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }


    /**
     * Returns true if both persons have the same name and phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && (otherPerson.getName().equals(getName()) && otherPerson.getPhone().equals(getPhone()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return id.equals(otherPerson.id)
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && bloodType.equals(otherPerson.bloodType)
                && dateOfBirth.equals(otherPerson.dateOfBirth);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, phone, email, bloodType, dateOfBirth);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("bloodType", bloodType)
                .add("dateOfBirth", dateOfBirth)
                .toString();
    }

}
