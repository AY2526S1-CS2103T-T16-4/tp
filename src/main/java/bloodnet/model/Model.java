package bloodnet.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import bloodnet.commons.core.GuiSettings;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<DonationRecord> PREDICATE_SHOW_ALL_DONATION_RECORDS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' persons file path.
     */
    Path getPersonListFilePath();

    /**
     * Sets the user prefs' persons file path.
     */
    void setPersonListFilePath(Path bloodNetFilePath);


    /**
     * Replaces persons data with the data in {@code personList}.
     */
    void setPersonList(ReadOnlyPersonList personList);

    /** Returns the PersonList */
    ReadOnlyPersonList getPersonList();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the bloodnet.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the bloodnet.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the bloodnet.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the bloodnet.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the bloodnet.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns the user prefs' donationRecords file path.
     */
    Path getDonationRecordListFilePath();

    /**
     * Sets the user prefs' donationRecords file path.
     */
    void setDonationRecordListFilePath(Path bloodNetFilePath);


    /**
     * Replaces donationRecords data with the data in {@code donationRecordList}.
     */
    void setDonationRecordList(ReadOnlyDonationRecordList donationRecordList);

    /** Returns the DonationRecordList */
    ReadOnlyDonationRecordList getDonationRecordList();

    /**
     * Returns true if a donationRecord with the same identity as {@code donationRecord} exists in the bloodnet.
     */
    boolean hasDonationRecord(DonationRecord donationRecord);

    /**
     * Deletes the given donationRecord.
     * The donationRecord must exist in the bloodnet.
     */
    void deleteDonationRecord(DonationRecord target);

    /**
     * Adds the given donationRecord.
     * {@code donationRecord} must not already exist in the bloodnet.
     */
    void addDonationRecord(DonationRecord donationRecord);

    /**
     * Replaces the given donationRecord {@code target} with {@code editedDonationRecord}.
     * {@code target} must exist in the bloodnet.
     * The donationRecord identity of {@code editedDonationRecord} must not be the same as another existing donationRecord in the bloodnet.
     */
    void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord);

    /** Returns an unmodifiable view of the filtered donationRecord list */
    ObservableList<DonationRecord> getFilteredDonationRecordList();

    /**
     * Updates the filter of the filtered donationRecord list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredDonationRecordList(Predicate<DonationRecord> predicate);
}
