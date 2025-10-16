package bloodnet.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import bloodnet.commons.core.GuiSettings;
import bloodnet.commons.core.LogsCenter;
import bloodnet.logic.commands.Command;
import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.exceptions.TerminalSessionStateException;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.PersonListParser;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.person.Person;
import bloodnet.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
        "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    public static final String TERMINAL_COMMAND_SESSION_STATE_ERROR_MESSAGE =
        "An error has occured under the hood! \n"
            + "Your previous command was likely not properly captured. Please try again.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final PersonListParser personListParser;

    private CommandSession currentSession = null;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and
     * {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        personListParser = new PersonListParser();
    }

    /**
     * Constructs a {@code LogicManager} with the given {@code Model},
     * {@code Storage} and {@code PersonListParser}.
     */
    public LogicManager(Model model, Storage storage, PersonListParser personListParser) {
        this.model = model;
        this.storage = storage;
        this.personListParser = personListParser;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        if (currentSession == null) {
            Command command = personListParser.parseCommand(commandText);
            this.currentSession = command.createSession(model);
        }

        assert this.currentSession != null;

        return advanceCurrentSession(commandText);
    }

    private CommandResult advanceCurrentSession(String input) throws CommandException {
        CommandResult result;
        try {
            result = currentSession.handle(input);
        } catch (TerminalSessionStateException e) {
            currentSession = null;
            result = new CommandResult(TERMINAL_COMMAND_SESSION_STATE_ERROR_MESSAGE);
        }
        if (currentSession != null && currentSession.isDone()) {
            currentSession = null;
            savePersonListSafely();
        }
        return result;
    }

    private void savePersonListSafely() throws CommandException {
        try {
            storage.savePersonList(model.getPersonList());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }

    @Override
    public ReadOnlyPersonList getPersonList() {
        return model.getPersonList();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getPersonListFilePath() {
        return model.getPersonListFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
