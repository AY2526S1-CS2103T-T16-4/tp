package bloodnet.logic;

import java.nio.file.Path;

import bloodnet.commons.core.GuiSettings;
import bloodnet.logic.commands.CommandResult;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.Model;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException   If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the PersonList.
     *
     * @see Model#getPersonList()
     */
    ReadOnlyPersonList getPersonList();

    /**
     * Returns an unmodifiable view of the filtered list of persons
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns the user prefs' bloodnet file path.
     */
    Path getPersonListFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
