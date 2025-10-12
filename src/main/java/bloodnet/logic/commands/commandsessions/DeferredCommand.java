package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.exceptions.CommandException;

/**
 * Represents a deferred command that can be executed later.
 */
@FunctionalInterface
public interface DeferredCommand {
    /**
     * Executes the deferred command.
     *
     * @return the {@link CommandResult} of executing the command.
     * @throws CommandException If an error occurs during execution of the command.
     */
    CommandResult run() throws CommandException;
}
