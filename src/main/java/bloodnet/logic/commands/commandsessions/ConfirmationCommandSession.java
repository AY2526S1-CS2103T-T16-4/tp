package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.exceptions.CommandException;

/**
 * Represents a command session that requires user confirmation before
 * proceeding with an action.
 * <p>
 * This session is typically used for commands that perform irreversible
 * operations, such as deleting data, where explicit user consent is needed
 * to continue.
 * </p>
 * <p>
 * The session manages a simple state machine that prompts the user for
 * confirmation ("yes" or "no") and handles the user's response accordingly.
 * </p>
 */
public class ConfirmationCommandSession implements CommandSession {
    private enum State {
        INITIAL {
            @Override
            Response handle(ConfirmationCommandSession session, String action, String input) {
                CommandResult prompt = new CommandResult(
                        String.format("Are you sure you want to %s? This action is not reversible.", action));
                return new Response(PENDING_CONFIRMATION, prompt);
            }
        },
        PENDING_CONFIRMATION {
            @Override
            Response handle(ConfirmationCommandSession session, String action, String input) throws CommandException {
                String normalisedInput = input.trim().toLowerCase();
                if ("yes".equals(normalisedInput)) {
                    CommandResult result = session.onConfirm.run();
                    return new Response(DONE, result);
                } else if ("no".equals(normalisedInput)) {
                    CommandResult result = new CommandResult(String.format("Operation cancelled. Did not %s.", action));
                    return new Response(DONE, result);
                } else {
                    CommandResult result = new CommandResult(
                            String.format("Please response with 'yes' or 'no'. Are you sure you want to %s?",
                                    session.action));
                    return new Response(this, result);
                }
            }
        },
        DONE {
            @Override
            Response handle(ConfirmationCommandSession session, String action, String input) {
                throw new IllegalStateException("Session is done, no more input expected.");
            }
        };

        abstract Response handle(ConfirmationCommandSession session, String action, String input)
                throws CommandException;

    }

    private record Response(State nextState, CommandResult result) {
    };

    private State currentState = State.INITIAL;
    private final String action;
    private final DeferredCommand onConfirm;

    /**
     * Constructs a {@code ConfirmationCommandSession} with the given action to be
     * executed and the {@code DeferredCommand}.
     */
    public ConfirmationCommandSession(String action, DeferredCommand onConfirm) {
        this.action = action;
        this.onConfirm = onConfirm;
    }

    @Override
    public CommandResult handle(String input) throws CommandException {
        Response response = currentState.handle(this, action, input);
        this.currentState = response.nextState();
        return response.result();
    }

    @Override
    public boolean isDone() {
        return this.currentState.equals(State.DONE);
    }
}
