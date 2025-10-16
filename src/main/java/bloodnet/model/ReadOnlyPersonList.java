package bloodnet.model;

import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of a person list
 */
public interface ReadOnlyPersonList {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
