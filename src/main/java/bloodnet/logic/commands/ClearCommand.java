package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.PersonList;

/**
 * Clears the bloodnet.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "PersonList has been cleared!";

    @Override
    public CommandSession createSession(Model model) throws CommandException {
        requireNonNull(model);
        return new ConfirmationCommandSession(COMMAND_WORD + " " + "bloodnet", () -> this.execute(model));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setPersonList(new PersonList());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
