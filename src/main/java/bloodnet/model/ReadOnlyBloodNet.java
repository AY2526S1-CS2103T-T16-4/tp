package bloodnet.model;

import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an bloodnet
 */
public interface ReadOnlyBloodNet {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
