package bloodnet.model;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import bloodnet.commons.core.GuiSettings;
import bloodnet.commons.core.LogsCenter;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the bloodnet data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final PersonList personList;
    private final DonationRecordList donationRecordList;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<DonationRecord> filteredDonationRecords;

    /**
     * Initializes a ModelManager with the given personList and userPrefs.
     */
    public ModelManager(ReadOnlyPersonList personList, ReadOnlyDonationRecordList donationRecordList, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(personList, userPrefs);

        logger.fine("Initializing with bloodnet: " + personList + " and user prefs " + userPrefs);

        this.personList = new PersonList(personList);
        this.donationRecordList = new DonationRecordList(donationRecordList);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.personList.getPersonList());
        filteredDonationRecords = new FilteredList<>(this.donationRecordList.getDonationRecordList());
    }

    public ModelManager() {
        this(new PersonList(), new DonationRecordList(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getPersonListFilePath() {
        return userPrefs.getPersonListFilePath();
    }

    @Override
    public void setPersonListFilePath(Path personListFilePath) {
        requireNonNull(personListFilePath);
        userPrefs.setPersonListFilePath(personListFilePath);
    }

    @Override
    public Path getDonationRecordListFilePath() {
        return userPrefs.getDonationRecordListFilePath();
    }

    @Override
    public void setDonationRecordListFilePath(Path donationRecordListFilePath) {
        requireNonNull(donationRecordListFilePath);
        userPrefs.setPersonListFilePath(donationRecordListFilePath);
    }

    //=========== PersonList ================================================================================

    @Override
    public void setPersonList(ReadOnlyPersonList personList) {
        this.personList.resetData(personList);
    }

    @Override
    public ReadOnlyPersonList getPersonList() {
        return personList;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return personList.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        personList.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        personList.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        personList.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedPersonList}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Donation Record List ================================================================================

    @Override
    public void setDonationRecordList(ReadOnlyDonationRecordList personList) {
        this.donationRecordList.resetData(donationRecordList);
    }

    @Override
    public ReadOnlyDonationRecordList getDonationRecordList() {
        return donationRecordList;
    }

    @Override
    public boolean hasDonationRecord(DonationRecord donationRecord) {
        requireNonNull(donationRecord);
        return donationRecordList.hasDonationRecord(donationRecord);
    }

    @Override
    public void deleteDonationRecord(DonationRecord target) {
        donationRecordList.removeDonationRecord(target);
    }

    @Override
    public void addDonationRecord(DonationRecord donationRecord) {
        donationRecordList.addDonationRecord(donationRecord);
        updateFilteredDonationRecordList(PREDICATE_SHOW_ALL_DONATION_RECORDS);
    }

    @Override
    public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
        requireAllNonNull(target, editedDonationRecord);

        donationRecordList.setDonationRecord(target, editedDonationRecord);
    }

    //=========== Filtered Donation Record List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code DonationRecord} backed by the internal list of
     * {@code versionedDonationRecordList}
     */
    @Override
    public ObservableList<DonationRecord> getFilteredDonationRecordList() {
        return filteredDonationRecords;
    }

    @Override
    public void updateFilteredDonationRecordList(Predicate<DonationRecord> predicate) {
        requireNonNull(predicate);
        filteredDonationRecords.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return personList.equals(otherModelManager.personList)
                && donationRecordList.equals(otherModelManager.donationRecordList)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredDonationRecords.equals(otherModelManager.filteredDonationRecords);
    }

}
