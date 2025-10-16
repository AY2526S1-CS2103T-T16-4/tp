package bloodnet.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.person.Person;

/**
 * An Immutable PersonList that is serializable to JSON format.
 */
@JsonRootName(value = "persons")
class JsonSerializablePersonList {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializablePersonList} with the given persons.
     */
    @JsonCreator
    public JsonSerializablePersonList(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyPersonList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializablePersonList}.
     */
    public JsonSerializablePersonList(ReadOnlyPersonList source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this bloodnet into the model's {@code PersonList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public PersonList toModelType() throws IllegalValueException {
        PersonList personList = new PersonList();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (personList.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            personList.addPerson(person);
        }
        return personList;
    }

}
