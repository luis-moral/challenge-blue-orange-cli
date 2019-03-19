package es.molabs.bocli;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.bocli.ouput.ConsoleOutput;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.CommandParser;
import es.molabs.bocli.parser.ConsoleCommandParser;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class CliFeature {

    @Spy
    private Output output = new ConsoleOutput();

    private WireMockServer apiMock;
    private CommandParser<String[]> commandParser;

    @Before
    public void setUp() {
        apiMock = new WireMockServer(80);
        apiMock.start();

        commandParser = new ConsoleCommandParser(output);
    }

    @Test public void 
    user_can_query_creators_with_a_filter_and_sorting() throws IOException {
        stubApi();

        String[] args = {"list", "-filter", "comics", "3", "-sort", "fullName"};

        commandParser.parse(args);

        Mockito
            .verify(output, Mockito.times(1))
            .printLine(readFile("/creator/get_two_creators.json"));
    }

    private void stubApi() throws IOException {
        apiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/v1/public/creators"))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(readFile("/creator/get_two_creators.json"))
                    )
            );
    }

    private String readFile(String resource) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8);
    }
}