package bloodnet.testutil;

import static bloodnet.testutil.TypicalPersons.ALICE;

import java.util.UUID;

import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * A utility class to help with building Person objects.
 */
public class DonationRecordBuilder {

    public static final Person DEFAULT_PERSON = ALICE;
    public static final String DEFAULT_DONATION_DATE = "19-10-2025";
    public static final String DEFAULT_BLOOD_VOLUME = "450";
    public static final String DEFAULT_BLOOD_TYPE = "B+";
    public static final String DEFAULT_DATE_OF_BIRTH = "14-02-2000";

    private UUID id;
    private UUID personId;
    private DonationDate donationDate;
    private BloodVolume bloodVolume;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public DonationRecordBuilder() {
        personId = DEFAULT_PERSON.getId();
        donationDate = new DonationDate(DEFAULT_DONATION_DATE);
        bloodVolume = new BloodVolume(DEFAULT_BLOOD_VOLUME);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public DonationRecordBuilder(DonationRecord donationRecordToCopy) {
        id = donationRecordToCopy.getId();
        personId = donationRecordToCopy.getPersonId();
        donationDate = donationRecordToCopy.getDonationDate();
        bloodVolume = donationRecordToCopy.getBloodVolume();
    }

    /**
     * Sets the {@code ID} of the {@code Person} that we are building.
     */
    public DonationRecordBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code PersonId} of the {@code Person} that we are building.
     */
    public DonationRecordBuilder withPersonId(UUID personId) {
        this.personId = personId;
        return this;
    }

    /**
     * Sets the {@code DonationDate} of the {@code Person} that we are building.
     */
    public DonationRecordBuilder withDonationDate(String donationDate) {
        this.donationDate = new DonationDate(donationDate);
        return this;
    }

    /**
     * Sets the {@code BloodVolume} of the {@code Person} that we are building.
     */
    public DonationRecordBuilder withBloodVolume(String bloodVolume) {
        this.bloodVolume = new BloodVolume(bloodVolume);
        return this;
    }

    public DonationRecord build() {
        return new DonationRecord(id, personId, donationDate, bloodVolume);
    }

}
