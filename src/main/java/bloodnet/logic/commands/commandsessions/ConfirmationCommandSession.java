package bloodnet.logic.commands.commandsessions;

import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
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
                        String.format(ConfirmationCommandSession.MESSAGE_SEEK_CONFIRMATION, action));
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
                    CommandResult result = new CommandResult(String.format(MESSAGE_CANCELLED, action));
                    return new Response(DONE, result);
                } else {
                    CommandResult result = new CommandResult(
                            String.format(MESSAGE_INVALID_INPUT,
                                    session.action));
                    return new Response(this, result);
                }
            }
        },
        DONE {
            @Override
            Response handle(ConfirmationCommandSession session, String action, String input)
                    throws TerminalSessionStateException {
                throw new TerminalSessionStateException();
            }
        };

        abstract Response handle(ConfirmationCommandSession session, String action, String input)
                throws CommandException, TerminalSessionStateException;

    }

    private record Response(State nextState, CommandResult result) {
    };

    public static final String MESSAGE_SEEK_CONFIRMATION =
            "Are you sure you want to %s? This action is not reversible.";
    public static final String MESSAGE_INVALID_INPUT =
            "Please response with 'yes' or 'no'. Are you sure you want to %s?";
    public static final String MESSAGE_CANCELLED = "Operation cancelled. Did not %s.";

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
    public CommandResult handle(String input) throws CommandException, TerminalSessionStateException {
        Response response = currentState.handle(this, action, input);
        this.currentState = response.nextState();
        return response.result();
    }

    @Override
    public boolean isDone() {
        return this.currentState.equals(State.DONE);
    }
}
