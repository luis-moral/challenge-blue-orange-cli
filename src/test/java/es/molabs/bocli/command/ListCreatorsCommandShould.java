package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.ConsoleCommandParser;
import es.molabs.bocli.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ListCreatorsCommandShould {

    @Mock
    private Output output;
    @Mock
    private WebClient webClient;

    @Test public void
    print_the_creators_filtered_and_sorted() throws IOException {
        Map<String, String> queryString = new HashMap<>();
        queryString.put("comics", "3");
        queryString.put("sort", "fullName");

        String responseBody = TestUtils.readFile("/creator/get_two_creators.json");

        Mockito
            .when(webClient.get(ConsoleCommandParser.DEFAULT_HOST + "/api/creators", queryString))
            .thenReturn(responseBody);

        new ListCreatorsCommand(output, webClient, ConsoleCommandParser.DEFAULT_HOST, "comics", "3", "fullName").execute();

        Mockito
            .verify(webClient, Mockito.times(1))
            .get(ConsoleCommandParser.DEFAULT_HOST + "/api/creators", queryString);

        Mockito
            .verify(output, Mockito.times(1))
            .printLine(responseBody);
    }
}