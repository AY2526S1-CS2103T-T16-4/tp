package bloodnet.model.util;


import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;

/**
 * Contains utility methods for populating {@code PersonList} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(null, new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new BloodType("A+"), new DateOfBirth("28-03-1995")),
            new Person(null, new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new BloodType("AB+"), new DateOfBirth("01-01-2000")),
            new Person(null ,new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new BloodType("O-"), new DateOfBirth("04-01-2003")),
            new Person(null, new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new BloodType("B+"), new DateOfBirth("12-12-2000")),
            new Person(null, new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new BloodType("B-"), new DateOfBirth("09-06-1990")),
            new Person(null, new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new BloodType("O+"), new DateOfBirth("21-03-2004"))
        };
    }

    public static ReadOnlyPersonList getSamplePersonList() {
        PersonList sampleAb = new PersonList();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }



}
