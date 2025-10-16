package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.ReadOnlyDonationRecordList;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends PersonStorage, DonationRecordStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getPersonListFilePath();

    @Override
    Optional<ReadOnlyPersonList> readPersonList() throws DataLoadingException;

    @Override
    void savePersonList(ReadOnlyPersonList bloodNet) throws IOException;

    @Override
    Path getDonationRecordListFilePath();

    @Override
    Optional<ReadOnlyDonationRecordList> readDonationRecordList() throws DataLoadingException;

    @Override
    void saveDonationRecordList(ReadOnlyDonationRecordList bloodNet) throws IOException;

}
