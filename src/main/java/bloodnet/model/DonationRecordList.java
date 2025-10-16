package bloodnet.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.donationrecord.UniqueDonationRecordList;
import javafx.collections.ObservableList;


/**
 * Wrapper for list of donationRecords
 * Duplicates are not allowed (by .isSameDonationRecord comparison)
 */
public class DonationRecordList implements ReadOnlyDonationRecordList {

    private final UniqueDonationRecordList donationRecords;

    {
        donationRecords = new UniqueDonationRecordList();
    }

    public DonationRecordList() {
    }

    /**
     * Creates an DonationRecordList using the DonationRecords in the {@code toBeCopied}
     */
    public DonationRecordList(ReadOnlyDonationRecordList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the donationRecord list with {@code donationRecords}.
     * {@code donationRecords} must not contain duplicate donationRecords.
     */
    public void setDonationRecords(List<DonationRecord> donationRecords) {
        this.donationRecords.setDonationRecords(donationRecords);
    }

    /**
     * Resets the existing data of this {@code DonationRecordList} with {@code newData}.
     */
    public void resetData(ReadOnlyDonationRecordList newData) {
        requireNonNull(newData);

        setDonationRecords(newData.getDonationRecordList());
    }

    //// donationRecord-level operations

    /**
     * Returns true if a donationRecord with the same identity as {@code donationRecord} exists in the bloodnet.
     */
    public boolean hasDonationRecord(DonationRecord donationRecord) {
        requireNonNull(donationRecord);
        return donationRecords.contains(donationRecord);
    }

    /**
     * Adds a donationRecord to the bloodnet.
     * The donationRecord must not already exist in the bloodnet.
     */
    public void addDonationRecord(DonationRecord p) {
        donationRecords.add(p);
    }

    /**
     * Replaces the given donationRecord {@code target} in the list with {@code editedDonationRecord}.
     * {@code target} must exist in the bloodnet.
     * The donationRecord identity of {@code editedDonationRecord} must not be the same as another existing
     * donationRecord in the bloodnet.
     */
    public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
        requireNonNull(editedDonationRecord);

        donationRecords.setDonationRecord(target, editedDonationRecord);
    }

    /**
     * Removes {@code key} from this {@code DonationRecordList}.
     * {@code key} must exist in the bloodnet.
     */
    public void removeDonationRecord(DonationRecord key) {
        donationRecords.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("donationRecords", donationRecords)
            .toString();
    }

    @Override
    public ObservableList<DonationRecord> getDonationRecordList() {
        return donationRecords.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DonationRecordList)) {
            return false;
        }

        DonationRecordList otherBloodNet = (DonationRecordList) other;
        return donationRecords.equals(otherBloodNet.donationRecords);
    }

    @Override
    public int hashCode() {
        return donationRecords.hashCode();
    }
}
