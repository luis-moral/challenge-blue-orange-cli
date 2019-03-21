package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.ConsoleCommandParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class DeleteNoteCommandShould {

    @Mock
    private Output output;
    @Mock
    private WebClient webClient;

    @Test public void
    print_note_deleted_message() throws IOException {
        new DeleteNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 1).execute();

        Mockito
            .verify(webClient, Mockito.times(1))
            .delete(ConsoleCommandParser.DEFAULT_HOST + DeleteNoteCommand.PATH_CREATOR_NOTE, "1");
    }
}