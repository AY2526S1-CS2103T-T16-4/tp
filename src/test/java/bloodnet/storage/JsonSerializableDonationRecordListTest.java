package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.commons.util.JsonUtil;
import bloodnet.model.DonationRecordList;
import bloodnet.testutil.TypicalDonationRecords;

public class JsonSerializableDonationRecordListTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableDonationRecordListTest");
    private static final Path TYPICAL_DONATION_RECORDS_FILE = TEST_DATA_FOLDER.resolve("typicalDonationRecordList.json");
    private static final Path INVALID_DONATION_RECORD_FILE = TEST_DATA_FOLDER.resolve("invalidDonationRecordList.json");
    private static final Path DUPLICATE_DONATION_RECORD_FILE = TEST_DATA_FOLDER.resolve("duplicateDonationRecordList.json");

    @Test
    public void toModelType_typicalDonationRecordsFile_success() throws Exception {
        JsonSerializableDonationRecordList dataFromFile = JsonUtil.readJsonFile(TYPICAL_DONATION_RECORDS_FILE,
            JsonSerializableDonationRecordList.class).get();
        DonationRecordList bloodNetFromFile = dataFromFile.toModelType();
        DonationRecordList typicalDonationRecordList = TypicalDonationRecords.getTypicalDonationRecordList();
        assertEquals(bloodNetFromFile, typicalDonationRecordList);
    }

    @Test
    public void toModelType_invalidDonationRecordFile_throwsIllegalValueException() throws Exception {
        JsonSerializableDonationRecordList dataFromFile = JsonUtil.readJsonFile(INVALID_DONATION_RECORD_FILE,
            JsonSerializableDonationRecordList.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateDonationRecords_throwsIllegalValueException() throws Exception {
        JsonSerializableDonationRecordList dataFromFile = JsonUtil.readJsonFile(DUPLICATE_DONATION_RECORD_FILE,
            JsonSerializableDonationRecordList.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableDonationRecordList.MESSAGE_DUPLICATE_DONATION_RECORD,
            dataFromFile::toModelType);
    }

}
