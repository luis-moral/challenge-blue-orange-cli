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
public class CreateNoteCommandShould {

    @Mock
    private Output output;
    @Mock
    private WebClient webClient;

    @Test public void
    print_note_created_message() throws IOException {
        String requestBody =
            Json
                .object()
                .add("creatorId", 3)
                .add("note", "Some Note")
                .toString();

        String responseBody =
            Json
                .object()
                .add("id", 1)
                .add("creatorId", 3)
                .add("note", "Some Note")
                .toString();

        Mockito
            .when(webClient.post(ConsoleCommandParser.DEFAULT_HOST + "/api/creator/note", requestBody))
            .thenReturn(responseBody);

        new CreateNoteCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, 3, "Some Note").execute();

        Mockito
            .verify(webClient, Mockito.times(1))
            .post(ConsoleCommandParser.DEFAULT_HOST + "/api/creator/note", requestBody);

        Mockito
            .verify(output, Mockito.times(1))
            .printLine(responseBody);
    }
}