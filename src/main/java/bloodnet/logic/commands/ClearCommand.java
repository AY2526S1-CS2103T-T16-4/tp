package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.AddressBook;
import bloodnet.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    @Override
    public CommandSession createSession(Model model) throws CommandException {
        requireNonNull(model);
        return new ConfirmationCommandSession(COMMAND_WORD + " " + "address book", () -> this.execute(model));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
