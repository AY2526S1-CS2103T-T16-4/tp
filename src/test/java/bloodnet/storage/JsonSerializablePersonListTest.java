package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.commons.util.JsonUtil;
import bloodnet.model.PersonList;
import bloodnet.testutil.TypicalPersons;
import org.junit.jupiter.api.Test;

public class JsonSerializablePersonListTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializablePersonListTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonList.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonList.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonList.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializablePersonList dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
            JsonSerializablePersonList.class).get();
        PersonList bloodNetFromFile = dataFromFile.toModelType();
        PersonList typicalPersonsPersonList = TypicalPersons.getTypicalPersonList();
        assertEquals(bloodNetFromFile, typicalPersonsPersonList);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializablePersonList dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
            JsonSerializablePersonList.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializablePersonList dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
            JsonSerializablePersonList.class).get();
        assertThrows(IllegalValueException.class, JsonSerializablePersonList.MESSAGE_DUPLICATE_PERSON,
            dataFromFile::toModelType);
    }

}
