package bloodnet.logic.commands.commandsessions.exceptions;

public class TerminalSessionStateException extends Exception {
    public TerminalSessionStateException() {
        super("Session has reached terminal state and should not be handling new user inputs");
    }
}
