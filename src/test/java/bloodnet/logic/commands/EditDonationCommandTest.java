package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
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
import bloodnet.testutil.DonationRecordBuilder;


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
        Person person = new Person(UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"),
                new Name("Sally"), new Phone("12345678"), new Email("x@example.com"),
                new BloodType("A+"), new DateOfBirth("02-02-2001"));
        modelStub.addDonationRecord(new DonationRecord(UUID.fromString(
                "3a8590f5-c86b-418a-82b5-7d65fc5602e4"),
                UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"), new DonationDate("02-02-2020") ,
                new BloodVolume("500")));
        modelStub.addPerson(person);
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
        Person person = new Person(UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"),
                new Name("Sally"), new Phone("12345678"), new Email("x@example.com"),
                new BloodType("A+"), new DateOfBirth("02-02-2001"));
        modelStub.addDonationRecord(new DonationRecord(UUID.fromString(
                "3a8590f5-c86b-418a-82b5-7d65fc5602e4"),
                UUID.fromString("d21831a7-8eec-4c33-ab00-fa74b8c822f1"), new DonationDate("02-02-2020") ,
                new BloodVolume("500")));
        modelStub.addPerson(person);
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
        Model model = new ModelManager();
        assertThrows(NullPointerException.class, ()
                -> model.addDonationRecord(new DonationRecordBuilder().withPersonId(null).build()));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        EditDonationCommand editedDonationRecord = new EditDonationCommand(indexStub,
                new EditDonationRecordDescriptor());
        assertEquals(editedDonationRecord, editedDonationRecord);
    }

    @Test
    public void equals_sameDescriptor_returnsTrue() {
        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        assertEquals(descriptorStub, descriptorStub);
    }

    @Test
    public void equals_sameValuesInDescriptor_returnsTrue() {

        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        EditDonationRecordDescriptor secondDescriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);
        secondDescriptorStub.setBloodVolume(bloodVolumeStub);
        secondDescriptorStub.setDonationDate(donationDateStub);

        assert(descriptorStub.equals(secondDescriptorStub));
    }

    @Test
    public void equals_sameValuesInCommand_returnsTrue() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand editDonationCommandOne = new EditDonationCommand(indexStub, descriptorStub);
        EditDonationCommand editDonationCommandTwo = new EditDonationCommand(indexStub, descriptorStub);
        assert(editDonationCommandOne.equals(editDonationCommandTwo));
    }

    @Test
    public void equals_differentIndexInCommand_returnsFalse() {
        Index indexStub1 = Index.fromZeroBased(1);
        Index indexStub2 = Index.fromZeroBased(2);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();

        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand command1 = new EditDonationCommand(indexStub1, descriptorStub);
        EditDonationCommand command2 = new EditDonationCommand(indexStub2, descriptorStub);
        assertFalse(command1.equals(command2));
    }

    @Test
    public void toString_validCommand_returnsStringRepresentation() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        DonationDate donationDateStub = new DonationDate("01-01-2025");

        EditDonationRecordDescriptor descriptorStub =
                new EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        descriptorStub.setDonationDate(donationDateStub);

        EditDonationCommand command = new EditDonationCommand(indexStub, descriptorStub);
        String result = command.toString();

        assert(result.contains("donationDate"));
        assert(result.contains("bloodVolume"));
    }
}

