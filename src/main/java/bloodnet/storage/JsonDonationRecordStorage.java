package bloodnet.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.commons.util.FileUtil;
import bloodnet.commons.util.JsonUtil;
import bloodnet.model.DonationRecordList;
import bloodnet.model.ReadOnlyDonationRecordList;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * A class to access DonationRecord data stored as a json file on the hard disk.
 */
public class JsonDonationRecordStorage implements DonationRecordStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonDonationRecordStorage.class);

    private Path filePath;

    public JsonDonationRecordStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getDonationRecordListFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyDonationRecordList> readDonationRecordList() throws DataLoadingException {
        return readDonationRecordList(filePath);
    }

    /**
     * Similar to {@link #readDonationRecordList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyDonationRecordList> readDonationRecordList(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableDonationRecordList> jsonDonationRecordList = JsonUtil.readJsonFile(
            filePath, JsonSerializableDonationRecordList.class);
        if (!jsonDonationRecordList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonDonationRecordList.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveDonationRecordList(ReadOnlyDonationRecordList donationRecordList) throws IOException {
        saveDonationRecordList(donationRecordList, filePath);
    }

    /**
     * Similar to {@link #saveDonationRecordList(ReadOnlyDonationRecordList)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveDonationRecordList(ReadOnlyDonationRecordList donationRecordList, Path filePath)
        throws IOException {
        requireNonNull(donationRecordList);
        requireNonNull(filePath);

        // Iterate through each donationRecord and generate a UUID for it if it doesn't have an ID yet.
        DonationRecordList updatedDonationRecordList = new DonationRecordList();
        for (DonationRecord donationRecord : donationRecordList.getDonationRecordList()) {
            if (donationRecord.getId() == null) {
                donationRecord.setId(UUID.randomUUID());
            }
            updatedDonationRecordList.addDonationRecord(donationRecord);
        }

        FileUtil.createIfMissing(filePath);

        // Use the updated donationRecord list instead.
        JsonUtil.saveJsonFile(new JsonSerializableDonationRecordList(updatedDonationRecordList), filePath);
    }

}
