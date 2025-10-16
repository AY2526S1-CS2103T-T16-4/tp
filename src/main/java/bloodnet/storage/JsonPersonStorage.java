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
import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.person.Person;

/**
 * A class to access Person data stored as a json file on the hard disk.
 */
public class JsonPersonStorage implements PersonStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonPersonStorage.class);

    private Path filePath;

    public JsonPersonStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getPersonListFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyPersonList> readPersonList() throws DataLoadingException {
        return readPersonList(filePath);
    }

    /**
     * Similar to {@link #readPersonList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyPersonList> readPersonList(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializablePersonList> jsonPersonList = JsonUtil.readJsonFile(
                filePath, JsonSerializablePersonList.class);
        if (!jsonPersonList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonPersonList.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void savePersonList(ReadOnlyPersonList personList) throws IOException {
        savePersonList(personList, filePath);
    }

    /**
     * Similar to {@link #savePersonList(ReadOnlyPersonList)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void savePersonList(ReadOnlyPersonList personList, Path filePath) throws IOException {
        requireNonNull(personList);
        requireNonNull(filePath);

        // Iterate through each person and generate a UUID for it if it doesn't have an ID yet.
        PersonList updatedPersonList = new PersonList();
        for (Person person : personList.getPersonList()) {
            if (person.getId() == null) {
                person.setId(UUID.randomUUID());
            }
            updatedPersonList.addPerson(person);
        }

        FileUtil.createIfMissing(filePath);

        // Use the updated person list instead.
        JsonUtil.saveJsonFile(new JsonSerializablePersonList(updatedPersonList), filePath);
    }

}
