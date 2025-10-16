package bloodnet.storage;

import static bloodnet.testutil.TypicalPersons.getTypicalPersonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import bloodnet.model.PersonList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.commons.core.GuiSettings;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonPersonStorage personStorage = new JsonPersonStorage(getTempFilePath("ab"));
        JsonDonationRecordStorage donationRecordStorage = new JsonDonationRecordStorage(getTempFilePath("donationRecords"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(personStorage, donationRecordStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void personReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonPersonStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonPersonStorageTest} class.
         */
        PersonList original = getTypicalPersonList();
        storageManager.savePersonList(original);
        ReadOnlyPersonList retrieved = storageManager.readPersonList().get();
        assertEquals(original, new PersonList(retrieved));
    }

    @Test
    public void getPersonListFilePath() {
        assertNotNull(storageManager.getPersonListFilePath());
    }

}
