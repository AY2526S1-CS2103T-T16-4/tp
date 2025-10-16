package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.DonationRecordList;
import bloodnet.model.ReadOnlyDonationRecordList;

/**
 * Represents a storage for {@link DonationRecordList}.
 */
public interface DonationRecordStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getDonationRecordListFilePath();

    /**
     * Returns DonationRecordList data as a {@link ReadOnlyDonationRecordList}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyDonationRecordList> readDonationRecordList() throws DataLoadingException;

    /**
     * @see #getDonationRecordListFilePath()
     */
    Optional<ReadOnlyDonationRecordList> readDonationRecordList(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyDonationRecordList} to the storage.
     * @param bloodNet cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveDonationRecordList(ReadOnlyDonationRecordList bloodNet) throws IOException;

    /**
     * @see #saveDonationRecordList(ReadOnlyDonationRecordList)
     */
    void saveDonationRecordList(ReadOnlyDonationRecordList bloodNet, Path filePath) throws IOException;

}
