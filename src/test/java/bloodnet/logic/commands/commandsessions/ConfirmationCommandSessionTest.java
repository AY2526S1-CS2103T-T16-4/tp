package bloodnet.logic.commands.commandsessions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;

public class ConfirmationCommandSessionTest {
    private Model model = new ModelManager();

    @Test
    public void handle_intialInput_seekConfirmation() throws CommandException, TerminalSessionStateException {
        Command commandStub = new CommandStub();
        ConfirmationCommandSession session = new ConfirmationCommandSession("action", () -> commandStub.execute(model));

        CommandResult result = session.handle("");

        assertEquals(
                String.format(ConfirmationCommandSession.MESSAGE_SEEK_CONFIRMATION, "action"),
                result.getFeedbackToUser());

        assertEquals(false, session.isDone());
    }

    @Test
    public void handle_positiveConfirmation_success() throws CommandException, TerminalSessionStateException {
        Command commandStub = new CommandStub();
        ConfirmationCommandSession session = new ConfirmationCommandSession("action", () -> commandStub.execute(model));

        session.handle("");
        CommandResult result = session.handle("yEs");

        assertEquals("Success", result.getFeedbackToUser());
        assertTrue(session.isDone());
    }

    @Test
    public void handle_negativeConfirmation_cancelled() throws CommandException, TerminalSessionStateException {
        Command commandStub = new CommandStub();
        ConfirmationCommandSession session = new ConfirmationCommandSession("action", () -> commandStub.execute(model));

        session.handle("");
        CommandResult result = session.handle("nO");

        assertEquals(String.format(ConfirmationCommandSession.MESSAGE_CANCELLED, "action"), result.getFeedbackToUser());
        assertTrue(session.isDone());
    }

    @Test
    public void handle_invalidConfirmation_invalidInput() throws CommandException, TerminalSessionStateException {
        Command commandStub = new CommandStub();
        ConfirmationCommandSession session = new ConfirmationCommandSession("action", () -> commandStub.execute(model));

        session.handle("");
        CommandResult result = session.handle("");

        assertEquals(
                String.format(ConfirmationCommandSession.MESSAGE_INVALID_INPUT, "action"),
                result.getFeedbackToUser());
        assertFalse(session.isDone());
    }

    @Test
    public void handle_terminalState_throwTerminalSessionStateException()
            throws CommandException, TerminalSessionStateException {
        Command commandStub = new CommandStub();
        ConfirmationCommandSession session = new ConfirmationCommandSession("action", () -> commandStub.execute(model));

        session.handle("");
        session.handle("yes");

        assertThrows(TerminalSessionStateException.class, () -> session.handle(""));
    }

    /**
     * A command stub that have a dummy execute method.
     */
    private class CommandStub extends Command {
        @Override
        public CommandSession createSession(Model mdoel) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("Success", false, false);
        }
    }
}
