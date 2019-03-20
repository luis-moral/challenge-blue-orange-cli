package es.molabs.bocli.parser;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.*;
import es.molabs.bocli.ouput.Output;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleCommandParserShould {

    @Mock
    private Output output;
    @Mock
    private WebClient webClient;

    private ConsoleCommandParser commandParser;

    @Before
    public void setUp() {
        commandParser = new ConsoleCommandParser(output, webClient);
    }

    @Test public void
    parse_list_creator_commands() {
        String[] noArguments = {"-list"};
        String[] onlyFilter = {"-list", "-filter", "comics=3"};
        String[] onlySorting = {"-list", "-sort", "fullName"};
        String[] allArguments = {"-list", "-filter", "comics=3", "-sort", "fullName"};

        Command noArgumentsCommand = commandParser.parse(noArguments);
        Command onlyFilterCommand = commandParser.parse(onlyFilter);
        Command onlySortingCommand = commandParser.parse(onlySorting);
        Command allArgumentsCommand = commandParser.parse(allArguments);

        Assertions
            .assertThat(noArgumentsCommand)
            .isEqualTo(new ListCreatorsCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, null, null, null));

        Assertions
            .assertThat(onlyFilterCommand)
            .isEqualTo(new ListCreatorsCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, "comics", "3", null));

        Assertions
            .assertThat(onlySortingCommand)
            .isEqualTo(new ListCreatorsCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, null, null, "fullName"));

        Assertions
            .assertThat(allArgumentsCommand)
            .isEqualTo(new ListCreatorsCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, "comics", "3", "fullName"));
    }

    @Test public void
    parse_create_note_commands() {
        String[] args = {"-add_note", "-creatorId", "5", "-note", "Some text"};

        Command createNoteCommand = commandParser.parse(args);

        Assertions
            .assertThat(createNoteCommand)
            .isEqualTo(new CreateNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 5, "Some text"));
    }
    
    @Test public void
    parse_edit_note_commands() {
        String[] args = {"-edit_note", "-id", "10", "-note", "Some text"};

        Command createNoteCommand = commandParser.parse(args);

        Assertions
            .assertThat(createNoteCommand)
            .isEqualTo(new EditNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 10, "Some text"));
    }
    
    @Test public void 
    parse_delete_note_commands() {
        String[] args = {"-delete_note", "-id", "15"};

        Command createNoteCommand = commandParser.parse(args);

        Assertions
            .assertThat(createNoteCommand)
            .isEqualTo(new DeleteNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 15));
    }
}