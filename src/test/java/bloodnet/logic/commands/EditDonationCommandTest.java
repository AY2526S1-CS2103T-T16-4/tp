package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;

public class EditDonationCommandTest {

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
        Model modelStub = new ModelManager();
        Person p = new Person(UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"),
                new Name("Sally"), new Phone("12345678"), new Email("x@example.com"),
                new BloodType("A+"), new DateOfBirth("02-02-2001"));
        modelStub.addDonationRecord(new DonationRecord(UUID.fromString(
                "3a8590f5-c86b-418a-82b5-7d65fc5602e4"),
                UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"), new DonationDate("02-02-2020") ,
                new BloodVolume("500")));
        modelStub.addPerson(p);
        Index indexStub = Index.fromZeroBased(0);
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        BloodVolume bloodVolumeStub = new BloodVolume("450");
        EditDonationCommand.EditDonationRecordDescriptor edit =
                new EditDonationCommand.EditDonationRecordDescriptor();
        edit.setDonationDate(donationDateStub);
        edit.setBloodVolume(bloodVolumeStub);
        EditDonationCommand editDonationCommand =
                new EditDonationCommand(indexStub, edit);
        CommandResult commandResult = editDonationCommand.execute(modelStub);
    }

    @Test
    public void execute_duplicateDonationRecord_success() throws Exception {
        Model modelStub = new ModelManager();
        Person p = new Person(UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"),
                new Name("Sally"), new Phone("12345678"), new Email("x@example.com"),
                new BloodType("A+"), new DateOfBirth("02-02-2001"));
        modelStub.addDonationRecord(new DonationRecord(UUID.fromString(
                "3a8590f5-c86b-418a-82b5-7d65fc5602e4"),
                UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"), new DonationDate("02-02-2020") ,
                new BloodVolume("500")));
        modelStub.addPerson(p);
        Index indexStub = Index.fromZeroBased(0);
        DonationDate donationDateStub = new DonationDate("02-02-2020");
        BloodVolume bloodVolumeStub = new BloodVolume("500");
        EditDonationCommand.EditDonationRecordDescriptor edit =
                new EditDonationCommand.EditDonationRecordDescriptor();
        edit.setDonationDate(donationDateStub);
        edit.setBloodVolume(bloodVolumeStub);
        EditDonationCommand editDonationCommand =
                new EditDonationCommand(indexStub, edit);
        CommandException exception = assertThrows(CommandException.class, () -> editDonationCommand.execute(modelStub));
        assert(exception.toString().contains(EditDonationCommand.MESSAGE_DUPLICATE_DONATION_RECORD));

    }

    @Test
    public void execute_personIdIsNull_failure() throws Exception {
        Model modelStub = new ModelManager();
        assertThrows(NullPointerException.class, () -> modelStub.addDonationRecord(new DonationRecord(UUID.fromString(
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
        EditDonationCommand edit = new EditDonationCommand(indexStub, descriptorStub);
        assert(edit.equals(edit));
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
    public void equals_compareNullDescriptor_returnsFalse() {
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        assert(!descriptorStub.equals(null));
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
    public void equals_null_returnsFalse() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        EditDonationCommand command = new EditDonationCommand(indexStub, descriptorStub);

        assert(!command.equals(null));
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

