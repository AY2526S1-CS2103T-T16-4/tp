package bloodnet.testutil;

import bloodnet.model.PersonList;
import bloodnet.model.person.Person;

/**
 * A utility class to help with building bloodNet objects.
 * Example usage: <br>
 *     {@code PersonList ab = new PersonListBuilder().withPerson("John", "Doe").build();}
 */
public class PersonListBuilder {

    private PersonList bloodNet;

    public PersonListBuilder() {
        bloodNet = new PersonList();
    }

    public PersonListBuilder(PersonList bloodNet) {
        this.bloodNet = bloodNet;
    }

    /**
     * Adds a new {@code Person} to the {@code PersonList} that we are building.
     */
    public PersonListBuilder withPerson(Person person) {
        bloodNet.addPerson(person);
        return this;
    }

    public PersonList build() {
        return bloodNet;
    }
}
