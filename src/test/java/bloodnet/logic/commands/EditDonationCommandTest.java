package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.GuiSettings;
import bloodnet.commons.core.index.Index;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import bloodnet.testutil.TypicalPersons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditDonationCommandTest {

    @Test
    public void constructor_nullBloodVolume_throwsNullPointerException() {
        Index indexStub = Index.fromZeroBased(0);
        DonationDate donationDateStub = new DonationDate("01-01-2025");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setDonationDate(donationDateStub);
        assertThrows(NullPointerException.class, () -> descriptorStub.setBloodVolume(null));
    }

    @Test
    public void constructor_nullDonationDate_throwsNullPointerException() {
        Index indexStub = Index.fromZeroBased(0);
        BloodVolume bloodVolumeStub = new BloodVolume("300");
        EditDonationCommand.EditDonationRecordDescriptor descriptorStub =
                new EditDonationCommand.EditDonationRecordDescriptor();
        descriptorStub.setBloodVolume(bloodVolumeStub);
        assertThrows(NullPointerException.class, () -> descriptorStub.setDonationDate(null));
    }

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

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getBloodNetFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setBloodNetFilePath(Path bloodNetFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setBloodNet(ReadOnlyBloodNet newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyBloodNet getBloodNet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addDonationRecord(DonationRecord donationRecord) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDonationRecord(DonationRecord donationRecord) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteDonationRecord(DonationRecord target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<DonationRecord> getFilteredDonationRecordList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredDonationRecordList(Predicate<DonationRecord> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson() {
            this.person = TypicalPersons.ALICE;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(person);
        }

        @Override
        public boolean hasDonationRecord(DonationRecord donationRecord) {
            return false;
        }

        @Override
        public void addDonationRecord(DonationRecord donationRecord) {
            // Do nothing
        }
    }
}
