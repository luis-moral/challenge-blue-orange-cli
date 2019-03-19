package es.molabs.bocli.parser;

import es.molabs.bocli.command.Command;
import es.molabs.bocli.command.ListCreatorsCommand;
import es.molabs.bocli.ouput.Output;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandParserShould {

    @Mock
    private Output output;

    private SystemCommandParser commandParser;

    @Before
    public void setUp() {
        commandParser = new SystemCommandParser(output);
    }

    @Test public void
    parse_list_creator_commands() {
        String[] args = {"list", "-filter", "comics", "3", "-sort", "fullName"};

        Command command = commandParser.parse(args);

        ListCreatorsCommand listCreatorsCommand
            = new ListCreatorsCommand(output, "comics", "fullName");

        Assertions
            .assertThat(command)
            .isEqualTo(listCreatorsCommand);
    }
}