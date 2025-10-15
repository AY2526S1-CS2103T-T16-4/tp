package bloodnet.model;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.getTypicalPersonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.model.person.Person;
import bloodnet.model.person.exceptions.DuplicatePersonException;
import bloodnet.testutil.PersonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonListTest {

    private final PersonList personList = new PersonList();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), personList.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> personList.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyPersonList_replacesData() {
        PersonList newData = getTypicalPersonList();
        personList.resetData(newData);
        assertEquals(newData, personList);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE)
                .withBloodType(VALID_BLOOD_TYPE_BOB)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        PersonListStub newData = new PersonListStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> personList.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> personList.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInPersonList_returnsFalse() {
        assertFalse(personList.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInPersonList_returnsTrue() {
        personList.addPerson(ALICE);
        assertTrue(personList.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInPersonList_returnsTrue() {
        personList.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE)
                .withBloodType(VALID_BLOOD_TYPE_BOB)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        assertTrue(personList.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> personList.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = PersonList.class.getCanonicalName() + "{persons=" + personList.getPersonList() + "}";
        assertEquals(expected, personList.toString());
    }

    /**
     * A stub ReadOnlyPersonList whose persons list can violate interface constraints.
     */
    private static class PersonListStub implements ReadOnlyPersonList {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        PersonListStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
