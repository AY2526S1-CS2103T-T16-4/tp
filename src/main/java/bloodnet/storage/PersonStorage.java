package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyPersonList;

/**
 * Represents a storage for {@link PersonList}.
 */
public interface PersonStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getPersonListFilePath();

    /**
     * Returns PersonList data as a {@link ReadOnlyPersonList}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyPersonList> readPersonList() throws DataLoadingException;

    /**
     * @see #getPersonListFilePath()
     */
    Optional<ReadOnlyPersonList> readPersonList(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyPersonList} to the storage.
     * @param bloodNet cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void savePersonList(ReadOnlyPersonList bloodNet) throws IOException;

    /**
     * @see #savePersonList(ReadOnlyPersonList)
     */
    void savePersonList(ReadOnlyPersonList bloodNet, Path filePath) throws IOException;

}
