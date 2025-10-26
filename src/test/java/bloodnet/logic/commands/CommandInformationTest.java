package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandInformationTest {

    @Test
    public void create_commandInformationObject_success() {
        String description = AddCommand.DESCRIPTION;
        String parameters = AddCommand.PARAMETERS;
        String examples = AddCommand.EXAMPLE;
        CommandInformation commandInformation = new CommandInformation(description, parameters, examples);

        assertEquals(description, commandInformation.getDescription());
        assertEquals(parameters, commandInformation.getParameters());
        assertEquals(examples, commandInformation.getExample());
    }
}
