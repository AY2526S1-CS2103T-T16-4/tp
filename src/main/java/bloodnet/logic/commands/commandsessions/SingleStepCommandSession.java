package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;

/**
 * A command session that executes a command in a single step without requiring
 * additional user input.
 *
 * This is the default command session created for each command unless otherwise
 * specified.
 */
public class SingleStepCommandSession implements CommandSession {
    private final Command command;
    private final Model model;
    private boolean done = false;

    /**
     * Constructs a {@code SingleStepCommandSession} with the given {@code Command}
     * and {@code Model}.
     */
    public SingleStepCommandSession(Command command, Model model) {
        this.command = command;
        this.model = model;
    }

    @Override
    public CommandResult handle(String userInput) throws CommandException {
        if (this.done) {
            throw new IllegalStateException("Command already completed.");
        }
        this.done = true;
        return command.execute(model);
    }

    @Override
    public boolean isDone() {
        return this.done;
    }
}
