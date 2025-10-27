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
import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.donationrecord.UniqueDonationRecordList;
import bloodnet.model.person.Person;
import bloodnet.model.person.UniquePersonList;

/**
 * A class to access BloodNet data stored as a json file on the hard disk.
 */
public class JsonBloodNetStorage implements BloodNetStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonBloodNetStorage.class);

    private Path filePath;

    public JsonBloodNetStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getBloodNetFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyBloodNet> readBloodNet() throws DataLoadingException {
        return readBloodNet(filePath);
    }

    /**
     * Similar to {@link #readBloodNet()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyBloodNet> readBloodNet(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableBloodNet> jsonBloodNet = JsonUtil.readJsonFile(
                filePath, JsonSerializableBloodNet.class);
        if (!jsonBloodNet.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonBloodNet.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveBloodNet(ReadOnlyBloodNet bloodNet) throws IOException {
        saveBloodNet(bloodNet, filePath);
    }

    /**
     * Similar to {@link #saveBloodNet(ReadOnlyBloodNet)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveBloodNet(ReadOnlyBloodNet bloodNet, Path filePath) throws IOException {
        requireNonNull(bloodNet);
        requireNonNull(filePath);

        // Add IDs to newly created entities
        // It must be done here as opposed to in the constructor of the respective models because models are an
        // in-memory representation of objects in the database.
        // As such, if we add the IDs on model instantiation, we could end up in a situation
        // where duplicating a model for the purposes of computation would result in
        // 2 models representing the same person but having different IDs

        // In fact, my original approach was to add the IDs during the construction stage,
        // but it caused many a test cases to fail as a result of the reason mentioned above.
        // The only way we could add IDs during the construction stage without
        // causing test cases to fail is to remove the "id" field from the equals() function,
        // but that also kind of doesn't make sense.

        BloodNet updatedBloodNet = new BloodNet();

        // Iterate through each person and generate a UUID for it if it doesn't have an ID yet.
        UniquePersonList updatedPersonList = new UniquePersonList();
        for (Person person : bloodNet.getPersonList()) {
            if (person.getId() == null) {
                person.setId(UUID.randomUUID());
            }
            updatedPersonList.add(person);
        }

        // Iterate through each donationRecord and generate a UUID for it if it doesn't have an ID yet.
        UniqueDonationRecordList updatedDonationRecordList = new UniqueDonationRecordList();
        for (DonationRecord donationRecord : bloodNet.getDonationRecordList()) {
            if (donationRecord.getId() == null) {
                donationRecord.setId(UUID.randomUUID());
            }
            updatedDonationRecordList.add(donationRecord);
        }

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableBloodNet(bloodNet), filePath);
    }

}
