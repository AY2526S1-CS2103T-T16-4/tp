package bloodnet.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.DonationRecordList;
import bloodnet.model.ReadOnlyDonationRecordList;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * An Immutable DonationRecordList that is serializable to JSON format.
 */
@JsonRootName(value = "donationRecords")
class JsonSerializableDonationRecordList {

    public static final String MESSAGE_DUPLICATE_DONATION_RECORD = "DonationRecords list contains duplicate records.";

    private final List<JsonAdaptedDonationRecord> donationRecords = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableDonationRecordList} with the given donationRecords.
     */
    @JsonCreator
    public JsonSerializableDonationRecordList(@JsonProperty("donationRecords") List<JsonAdaptedDonationRecord>
                                                  donationRecords) {
        this.donationRecords.addAll(donationRecords);
    }

    /**
     * Converts a given {@code ReadOnlyDonationRecordList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableDonationRecordList}.
     */
    public JsonSerializableDonationRecordList(ReadOnlyDonationRecordList source) {
        donationRecords.addAll(source.getDonationRecordList().stream().map(JsonAdaptedDonationRecord::new)
            .collect(Collectors.toList()));
    }

    /**
     * Converts this bloodnet into the model's {@code DonationRecordList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public DonationRecordList toModelType() throws IllegalValueException {
        DonationRecordList donationRecordList = new DonationRecordList();
        for (JsonAdaptedDonationRecord jsonAdaptedDonationRecord : donationRecords) {
            DonationRecord donationRecord = jsonAdaptedDonationRecord.toModelType();
            if (donationRecordList.hasDonationRecord(donationRecord)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DONATION_RECORD);
            }
            donationRecordList.addDonationRecord(donationRecord);
        }
        return donationRecordList;
    }

}
