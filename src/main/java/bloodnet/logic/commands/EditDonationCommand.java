package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import bloodnet.commons.core.index.Index;
import bloodnet.commons.util.CollectionUtil;
import bloodnet.commons.util.ToStringBuilder;
import bloodnet.logic.Messages;
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

    public static final CommandInformation COMMAND_INFORMATION = new CommandInformation(COMMAND_WORD,
            "Edits the donation "
            + "record detail(s) identified by the index number used in the displayed donation record list. "
            + "At least one field must be provided.",
            "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DONATION_DATE + "DONATION DATE (DD-MM-YYYY)] "
            + "[" + PREFIX_BLOOD_VOLUME + "BLOOD VOLUME (IN MILLILITRES)]",
            "Example: editdonation 1 v/100 d/02-02-2002");

    public static final String MESSAGE_EDIT_DONATION_RECORD_SUCCESS = "Edited Donation Record: %1$s";

    public static final String MESSAGE_DUPLICATE_DONATION_RECORD =
            "No change to the donation record.";
    public static final String MESSAGE_CONCATENATED_VALIDATION_ERRORS_HEADER =
            "You are attempting to modify a donation record to an invalid one. "
            + "Please fix these errors:";

    private final Index indexOfDonationRecord;
    private final EditDonationRecordDescriptor editDonationRecordDescriptor;

    /**
     * @param indexOfDonationRecord Index of the donation record in the displayed donation record list.
     * @param editDonationRecordDescriptor Details related to the edits made to the donation record.
     */
    public EditDonationCommand(Index indexOfDonationRecord,
                               EditDonationRecordDescriptor editDonationRecordDescriptor) {
        requireNonNull(indexOfDonationRecord);
        requireNonNull(editDonationRecordDescriptor);

        this.indexOfDonationRecord = indexOfDonationRecord;
        this.editDonationRecordDescriptor = new EditDonationRecordDescriptor(editDonationRecordDescriptor);
    }

    @Override
    public InputResponse execute(Model model) throws CommandException {
        requireNonNull(model);
        DonationRecord recordToEdit = getDonationRecordToEdit(model);
        Person personForRecordEdit = getPersonToEditRecordFor(model, recordToEdit);

        assert personForRecordEdit != null;
        UUID personId = personForRecordEdit.getId();
        assert personId != null;
        DonationRecord editedDonationRecord = createEditedDonationRecord(recordToEdit, editDonationRecordDescriptor);

        ArrayList<String> validationErrorStrings = editedDonationRecord.validate(model);
        if (!validationErrorStrings.isEmpty()) {
            String concatenatedMessage = MESSAGE_CONCATENATED_VALIDATION_ERRORS_HEADER;
            for (String validationErrorString : validationErrorStrings) {
                concatenatedMessage += "\n- " + validationErrorString;
            }
            throw new CommandException(concatenatedMessage);
        }

        if (recordToEdit.equals(editedDonationRecord)) {
            throw new CommandException(MESSAGE_DUPLICATE_DONATION_RECORD);
        }

        model.setDonationRecord(recordToEdit, editedDonationRecord);
        return new InputResponse(String.format(MESSAGE_EDIT_DONATION_RECORD_SUCCESS,
                Messages.format(editedDonationRecord, personForRecordEdit)));
    }

    /**
     * Creates and returns a {@code DonationRecord} with the details of {@code personForRecordEdit}
     * edited with {@code editDonationRecordDescriptor}.
     */
    private static DonationRecord createEditedDonationRecord(
            DonationRecord donationRecordToEdit, EditDonationRecordDescriptor editedDonationRecordDescriptor) {
        DonationDate editedDonationDate =
                editedDonationRecordDescriptor.getDonationDate().orElse(donationRecordToEdit.getDonationDate());
        BloodVolume editedBloodVolume =
                editedDonationRecordDescriptor.getBloodVolume().orElse(donationRecordToEdit.getBloodVolume());

        return new DonationRecord(donationRecordToEdit.getId(),
                donationRecordToEdit.getPersonId(), editedDonationDate, editedBloodVolume);
    }

    private DonationRecord getDonationRecordToEdit(Model model) throws CommandException {
        requireNonNull(model);
        List<DonationRecord> donationRecordList = model.getFilteredDonationRecordList();
        if (indexOfDonationRecord.getZeroBased() >= donationRecordList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DONATION_DISPLAYED_INDEX);
        }
        return donationRecordList.get(indexOfDonationRecord.getZeroBased());
    }

    /**
     * Retrieves the {@code Person} corresponding to the {@code personId} of the provided {@code donationRecord}
     */
    private Person getPersonToEditRecordFor(Model model, DonationRecord donationRecord) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();
        Optional<Person> optionalPerson = personList.stream()
                .filter(person -> person.getId().equals(donationRecord.getPersonId())).findFirst();

        if (optionalPerson.isPresent()) {
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
        return indexOfDonationRecord.equals(otherEditDonationCommand.indexOfDonationRecord)
                && editDonationRecordDescriptor.equals(otherEditDonationCommand.editDonationRecordDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("indexOfDonationRecord", indexOfDonationRecord)
                .add("editDonationRecordDescriptor", editDonationRecordDescriptor)
                .toString();
    }

    public static String getMessageUsage() {
        return COMMAND_INFORMATION.getMessageUsage();
    }

    /**
     * Stores the details to edit the donation record with. Each non-empty field value will
     * replace the corresponding field value of the donation record.
     */
    public static class EditDonationRecordDescriptor {
        private BloodVolume bloodVolume;
        private DonationDate donationDate;

        public EditDonationRecordDescriptor() {
        }

        /**
         * Copy the constructor.
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
