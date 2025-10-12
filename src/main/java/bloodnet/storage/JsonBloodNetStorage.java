package bloodnet.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.commons.util.FileUtil;
import bloodnet.commons.util.JsonUtil;
import bloodnet.model.ReadOnlyBloodNet;

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

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableBloodNet(bloodNet), filePath);
    }

}
