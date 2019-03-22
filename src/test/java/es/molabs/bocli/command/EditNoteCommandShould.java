package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
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
public class EditNoteCommandShould {

    @Mock
    private Output output;
    @Mock
    private WebClient webClient;

    @Test public void
    print_note_edited_message() throws IOException {
        String requestBody =
            Json
                .object()
                .add("text", "Some Note")
                .toString();

        String responseBody =
            Json
                .object()
                .add("id", 1)
                .add("creatorId", 3)
                .add("text", "Some Note")
                .toString();

        Mockito
            .when(webClient.put(ConsoleCommandParser.DEFAULT_HOST + EditNoteCommand.PATH_CREATOR_NOTE, "1", requestBody))
            .thenReturn(responseBody);

        new EditNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 1, "Some Note").execute();

        Mockito
            .verify(webClient, Mockito.times(1))
            .put(ConsoleCommandParser.DEFAULT_HOST + EditNoteCommand.PATH_CREATOR_NOTE, "1", requestBody);

        Mockito
            .verify(output, Mockito.times(1))
            .printLine(responseBody);
    }
}