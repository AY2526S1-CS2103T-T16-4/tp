package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalDonationRecords.ALICE_DONATION_RECORDS;
import static bloodnet.testutil.TypicalDonationRecords.BENSON_DONATION_RECORDS;
import static bloodnet.testutil.TypicalDonationRecords.CARL_DONATION_RECORDS;
import static bloodnet.testutil.TypicalDonationRecords.getTypicalDonationRecordList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.DonationRecordList;
import bloodnet.model.ReadOnlyDonationRecordList;

public class JsonDonationRecordStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonDonationRecordStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readDonationRecordList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readDonationRecordList(null));
    }

    private java.util.Optional<ReadOnlyDonationRecordList> readDonationRecordList(String filePath) throws Exception {
        return new JsonDonationRecordStorage(Paths.get(filePath)).readDonationRecordList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readDonationRecordList("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readDonationRecordList("notJsonFormatDonationRecordList.json"));
    }

    @Test
    public void readDonationRecordList_invalidDonationRecordDonationRecordList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readDonationRecordList("invalidDonationRecordList.json"));
    }

    @Test
    public void readDonationRecordList_invalidAndValidDonationRecordDonationRecordList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readDonationRecordList("invalidAndValidDonationRecordList.json"));
    }

    @Test
    public void readAndSaveDonationRecordList_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempDonationRecordList.json");
        DonationRecordList original = getTypicalDonationRecordList();
        JsonDonationRecordStorage jsonDonationRecordListStorage = new JsonDonationRecordStorage(filePath);

        // Save in new file and read back
        jsonDonationRecordListStorage.saveDonationRecordList(original, filePath);
        ReadOnlyDonationRecordList readBack = jsonDonationRecordListStorage.readDonationRecordList(filePath).get();
        assertEquals(original, new DonationRecordList(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addDonationRecord(CARL_DONATION_RECORDS.get(0));
        original.removeDonationRecord(ALICE_DONATION_RECORDS.get(0));
        jsonDonationRecordListStorage.saveDonationRecordList(original, filePath);
        readBack = jsonDonationRecordListStorage.readDonationRecordList(filePath).get();
        assertEquals(original, new DonationRecordList(readBack));

        // Save and read without specifying file path
        original.addDonationRecord(CARL_DONATION_RECORDS.get(1));
        jsonDonationRecordListStorage.saveDonationRecordList(original); // file path not specified
        readBack = jsonDonationRecordListStorage.readDonationRecordList().get(); // file path not specified
        assertEquals(original, new DonationRecordList(readBack));

    }

    @Test
    public void saveDonationRecordList_nullDonationRecordList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveDonationRecordList(null, "SomeFile.json"));
    }

    /**
     * Saves {@code bloodNet} at the specified {@code filePath}.
     */
    private void saveDonationRecordList(ReadOnlyDonationRecordList bloodNet, String filePath) {
        try {
            new JsonDonationRecordStorage(Paths.get(filePath))
                .saveDonationRecordList(bloodNet, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveDonationRecordList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveDonationRecordList(new DonationRecordList(), null));
    }
}
