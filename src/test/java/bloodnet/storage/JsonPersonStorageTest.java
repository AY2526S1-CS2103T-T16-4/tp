package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.HOON;
import static bloodnet.testutil.TypicalPersons.IDA;
import static bloodnet.testutil.TypicalPersons.getTypicalPersonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyPersonList;

public class JsonPersonStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonPersonStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readPersonList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readPersonList(null));
    }

    private java.util.Optional<ReadOnlyPersonList> readPersonList(String filePath) throws Exception {
        return new JsonPersonStorage(Paths.get(filePath)).readPersonList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readPersonList("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readPersonList("notJsonFormatPersonList.json"));
    }

    @Test
    public void readPersonList_invalidPersonPersonList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readPersonList("invalidPersonList.json"));
    }

    @Test
    public void readPersonList_invalidAndValidPersonPersonList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readPersonList("invalidAndValidPersonList.json"));
    }

    @Test
    public void readAndSavePersonList_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempPersonList.json");
        PersonList original = getTypicalPersonList();
        JsonPersonStorage jsonPersonListStorage = new JsonPersonStorage(filePath);

        // Save in new file and read back
        jsonPersonListStorage.savePersonList(original, filePath);
        ReadOnlyPersonList readBack = jsonPersonListStorage.readPersonList(filePath).get();
        assertEquals(original, new PersonList(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonPersonListStorage.savePersonList(original, filePath);
        readBack = jsonPersonListStorage.readPersonList(filePath).get();
        assertEquals(original, new PersonList(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonPersonListStorage.savePersonList(original); // file path not specified
        readBack = jsonPersonListStorage.readPersonList().get(); // file path not specified
        assertEquals(original, new PersonList(readBack));

    }

    @Test
    public void savePersonList_nullPersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> savePersonList(null, "SomeFile.json"));
    }

    /**
     * Saves {@code bloodNet} at the specified {@code filePath}.
     */
    private void savePersonList(ReadOnlyPersonList bloodNet, String filePath) {
        try {
            new JsonPersonStorage(Paths.get(filePath))
                .savePersonList(bloodNet, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void savePersonList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> savePersonList(new PersonList(), null));
    }
}
