package bloodnet.model;

import bloodnet.model.donationrecord.DonationRecord;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of a person list
 */
public interface ReadOnlyDonationRecordList {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<DonationRecord> getDonationRecordList();

}
