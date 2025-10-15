package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;

/**
 * Manages storage of PersonList data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private PersonStorage personStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code PersonStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(PersonStorage personStorage, UserPrefsStorage userPrefsStorage) {
        this.personStorage = personStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ PersonList methods ==============================

    @Override
    public Path getPersonListFilePath() {
        return personStorage.getPersonListFilePath();
    }

    @Override
    public Optional<ReadOnlyPersonList> readPersonList() throws DataLoadingException {
        return readPersonList(personStorage.getPersonListFilePath());
    }

    @Override
    public Optional<ReadOnlyPersonList> readPersonList(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return personStorage.readPersonList(filePath);
    }

    @Override
    public void savePersonList(ReadOnlyPersonList personList) throws IOException {
        savePersonList(personList, personStorage.getPersonListFilePath());
    }

    @Override
    public void savePersonList(ReadOnlyPersonList personList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        personStorage.savePersonList(personList, filePath);
    }

}
