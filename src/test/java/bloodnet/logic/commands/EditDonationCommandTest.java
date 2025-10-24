package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandFailure;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalDonationRecords.getTypicalBloodNet;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_DONATION;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import bloodnet.testutil.DonationRecordBuilder;
import bloodnet.testutil.EditDonationRecordsDescriptorBuilder;

public class EditDonationCommandTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void constructor_validArguments_success() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        new EditDonationCommand(indexStub, descriptorStub);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand edit = new EditDonationCommand(indexStub, descriptorStub);
        assertThrows(NullPointerException.class, () -> edit.execute(null));
    }

    @Test
    public void execute_validArguments_success() throws Exception {
        DonationRecord editedDonationRecord = new DonationRecordBuilder().build();
        EditDonationRecordDescriptor descriptor = new EditDonationRecordsDescriptorBuilder(editedDonationRecord)
                .build();

        Person personToEditRecordFor = model.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(editedDonationRecord.getPersonId()))
                .findFirst()
                .orElseThrow();

        EditDonationCommand editDonationCommand = new EditDonationCommand(INDEX_FIRST_DONATION, descriptor);

        String expectedMessage = String.format(
                EditDonationCommand.MESSAGE_EDIT_DONATION_RECORD_SUCCESS,
                Messages.format(editedDonationRecord, personToEditRecordFor));

        Model expectedModel = new ModelManager(new BloodNet(model.getBloodNet()), new UserPrefs());

        expectedModel.setDonationRecord(model.getFilteredDonationRecordList().get(0), editedDonationRecord);

        assertCommandSuccess(editDonationCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateDonationRecord_failure() throws Exception {
        DonationRecord firstDonationRecord = model.getFilteredDonationRecordList()
                .get(INDEX_FIRST_DONATION.getZeroBased());
        EditDonationRecordDescriptor descriptor = new EditDonationRecordsDescriptorBuilder(firstDonationRecord).build();
        EditDonationCommand editDonationCommand = new EditDonationCommand(INDEX_FIRST_DONATION, descriptor);

        assertCommandFailure(editDonationCommand, model, EditDonationCommand.MESSAGE_DUPLICATE_DONATION_RECORD);
    }

    @Test
    public void execute_personIdIsNull_failure() throws Exception {
        assertThrows(NullPointerException.class, () -> model.addDonationRecord(new DonationRecord(UUID.fromString(
               "3a8590f5-c86b-418a-82b5-7d65fc5602e4"),
               UUID.fromString(null), new DonationDate("02-02-2020") ,
               new BloodVolume("500"))));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand editedDonationRecord = new EditDonationCommand(indexStub, descriptorStub);
        assert(editedDonationRecord.equals(editedDonationRecord));
    }

    @Test
    public void equals_sameDescriptor_returnsTrue() {
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        assert(descriptorStub.equals(descriptorStub));
    }

    @Test
    public void equals_sameValuesInDescriptor_returnsTrue() {
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand.EditDonationRecordDescriptor secondDescriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        secondDescriptorStub.setBloodVolume(bloodVolumeStub);
        secondDescriptorStub.setDonationDate(donationDateStub);
        assert(descriptorStub.equals(secondDescriptorStub));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand command1 = new EditDonationCommand(indexStub, descriptorStub);
        EditDonationCommand command2 = new EditDonationCommand(indexStub, descriptorStub);
        assert(command1.equals(command2));
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        Index indexStub1 = Index.fromZeroBased(1);
        Index indexStub2 = Index.fromZeroBased(2);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand command1 = new EditDonationCommand(indexStub1, descriptorStub);
        EditDonationCommand command2 = new EditDonationCommand(indexStub2, descriptorStub);
        assert(!command1.equals(command2));
    }

    @Test
    public void toString_validCommand_returnsStringRepresentation() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand command = new EditDonationCommand(indexStub, descriptorStub);
        String result = command.toString();

        assert(result.contains("donationDate"));
        assert(result.contains("bloodVolume"));
    }
}

