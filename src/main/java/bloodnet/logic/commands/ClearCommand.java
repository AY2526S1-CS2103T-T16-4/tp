package bloodnet.logic.commands;

import static java.util.Objects.requireNonNull;

import bloodnet.model.BloodNet;
import bloodnet.model.Model;

/**
 * Clears the bloodnet.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "BloodNet has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setBloodNet(new BloodNet());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
