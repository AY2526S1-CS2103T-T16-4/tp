package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.commands.CommandTestUtil.DONATION_DATE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.DONATION_DATE_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_VOLUME_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_VOLUME_DESC_BOB;

import static bloodnet.logic.commands.CommandTestUtil.INVALID_BLOOD_VOLUME_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_DONATION_DATE_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_BLOOD_VOLUME;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_DONATION_DATE;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_VOLUME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_VOLUME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DONATION_DATE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DONATION_DATE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static bloodnet.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditCommand.EditPersonDescriptor;
import bloodnet.logic.commands.EditDonationsCommand;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Phone;
import bloodnet.testutil.DonationRecordBuilder;
import bloodnet.testutil.EditDonationRecordsDescriptorBuilder;
import bloodnet.testutil.EditPersonDescriptorBuilder;

public class EditDonationsCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDonationsCommand.MESSAGE_USAGE);

    private EditDonationsCommandParser parser = new EditDonationsCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, BLOOD_VOLUME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditDonationsCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + BLOOD_VOLUME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + BLOOD_VOLUME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_BLOOD_VOLUME_DESC, BloodVolume.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_DONATION_DATE_DESC, DonationDate.MESSAGE_CONSTRAINTS); // invalid phone

        // invalid blood volume followed by donation date
        assertParseFailure(parser, "1" + INVALID_BLOOD_VOLUME_DESC + INVALID_DONATION_DATE_DESC,
                BloodVolume.MESSAGE_CONSTRAINTS);

        // both fields are invalid, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_BLOOD_VOLUME_DESC
                        + INVALID_DONATION_DATE_DESC,
                BloodVolume.MESSAGE_CONSTRAINTS);
    }



    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + BLOOD_VOLUME_DESC_AMY + BLOOD_VOLUME_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + BLOOD_VOLUME_DESC_AMY + " " + INVALID_BLOOD_VOLUME_DESC;
        System.out.println("userInput: \"" + userInput + "\"");

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + DONATION_DATE_DESC_AMY + DONATION_DATE_DESC_BOB
                + BLOOD_VOLUME_DESC_AMY + BLOOD_VOLUME_DESC_BOB;
        System.out.println("userInput: \"" + userInput + "\"");

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME,
                        PREFIX_DONATION_DATE));
    }

}
