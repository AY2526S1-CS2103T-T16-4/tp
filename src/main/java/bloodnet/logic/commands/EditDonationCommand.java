package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.CollectionUtil;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;


/**
 * Allows users to edit the blood donation records of an existing person in BloodNet.
 */
public class EditDonationCommand extends Command {

    public static final String COMMAND_WORD = "editdonation";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the blood donation records of the person "
            + "identified by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DONATION_DATE + "DONATION_DATE] "
            + "[" + PREFIX_BLOOD_VOLUME + "BLOOD_VOLUME] ";

    public static final String MESSAGE_EDIT_DONATION_RECORD_SUCCESS = "Edited Donation Record: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_DONATION_RECORD = "This donation record already exists in the BloodNet.";

    private final Index index;
    private final EditDonationRecordDescriptor editDonationRecordDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditDonationCommand(Index index, EditDonationRecordDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);
        this.index = index;
        this.editDonationRecordDescriptor = new EditDonationRecordDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        DonationRecord recordToEdit = getDonationRecordToEdit(model);
        Person personToAddRecordFor = getPersonToEditRecordFor(model, recordToEdit);
        UUID personId = personToAddRecordFor.getId();

        assert personId != null;

        DonationRecord editedDonationRecord = createEditedPersonRecord(recordToEdit, editDonationRecordDescriptor);

        if (model.hasDonationRecord(editedDonationRecord) && recordToEdit.equals(editedDonationRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_DONATION_RECORD);
        }

        model.setDonationRecord(recordToEdit, editedDonationRecord);

        return new CommandResult(String.format(MESSAGE_EDIT_DONATION_RECORD_SUCCESS, Messages.format(editedDonationRecord,
                personToAddRecordFor)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static DonationRecord createEditedPersonRecord(
            DonationRecord personToEdit, EditDonationRecordDescriptor editDonationRecordDescriptor) {

        DonationDate updatedDonationDate =
                editDonationRecordDescriptor.getDonationDate().orElse(personToEdit.getDonationDate());
        BloodVolume updatedBloodVolume =
                editDonationRecordDescriptor.getBloodVolume().orElse(personToEdit.getBloodVolume());

        return new DonationRecord(null,
                personToEdit.getPersonId(), updatedDonationDate, updatedBloodVolume);
    }

    private DonationRecord getDonationRecordToEdit(Model model) throws CommandException {
        requireNonNull(model);
        List<DonationRecord> lastShownList = model.getFilteredDonationRecordList();

        if (index.getZeroBased() >= lastShownList.size()) {
            System.out.println("is it here");
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Retrieves the {@code Person} corresponding to the {@code targetPersonIndex} of this {@code AddDonationCommand}
     */
    private Person getPersonToEditRecordFor(Model model, DonationRecord donationRecord) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Optional<Person> optionalPerson = lastShownList.stream()
                .filter(person -> person.getId().equals(donationRecord.getPersonId())).findFirst();

        if (optionalPerson != null) {
            return optionalPerson.get();
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDonationCommand)) {
            return false;
        }

        EditDonationCommand otherEditDonationCommand = (EditDonationCommand) other;
        return index.equals(otherEditDonationCommand.index)
                && editDonationRecordDescriptor.equals(otherEditDonationCommand.editDonationRecordDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editDonationRecordDescriptor", editDonationRecordDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the donation record with. Each non-empty field value will
     * replace the
     * corresponding field value of the donation record.
     */
    public static class EditDonationRecordDescriptor {
        private BloodVolume bloodVolume;
        private DonationDate donationDate;


        public EditDonationRecordDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditDonationRecordDescriptor(EditDonationRecordDescriptor toCopy) {
            setBloodVolume(toCopy.bloodVolume);
            setDonationDate(toCopy.donationDate);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(bloodVolume, donationDate);
        }

        public void setBloodVolume(BloodVolume bloodVolume) {
            this.bloodVolume = bloodVolume;
        }

        public void setDonationDate(DonationDate donationDate) {
            this.donationDate = donationDate;
        }

        public Optional<DonationDate> getDonationDate() {
            return Optional.ofNullable(donationDate);
        }

        public Optional<BloodVolume> getBloodVolume() {
            return Optional.ofNullable(bloodVolume);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditDonationRecordDescriptor)) {
                return false;
            }

            EditDonationRecordDescriptor otherEditPersonDescriptor = (EditDonationRecordDescriptor) other;
            return Objects.equals(bloodVolume, otherEditPersonDescriptor.bloodVolume)
                    && Objects.equals(donationDate, otherEditPersonDescriptor.donationDate);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("bloodVolume", getBloodVolume())
                    .add("donationDate", getDonationDate())
                    .toString();
        }

    }
}
