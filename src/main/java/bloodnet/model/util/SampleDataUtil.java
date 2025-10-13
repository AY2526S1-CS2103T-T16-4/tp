package bloodnet.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;
import bloodnet.model.tag.Tag;

/**
 * Contains utility methods for populating {@code BloodNet} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new BloodType("A+"),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new BloodType("AB+"),
                getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new BloodType("O-"),
                getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new BloodType("B+"),
                getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new BloodType("B-"),
                getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new BloodType("O+"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyBloodNet getSampleBloodNet() {
        BloodNet sampleAb = new BloodNet();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
