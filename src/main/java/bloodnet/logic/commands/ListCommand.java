package bloodnet.logic.commands;

import static bloodnet.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static java.util.Objects.requireNonNull;

import bloodnet.model.Model;

/**
 * Lists all persons in the bloodnet to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public InputResponse execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new InputResponse(MESSAGE_SUCCESS);
    }
}
