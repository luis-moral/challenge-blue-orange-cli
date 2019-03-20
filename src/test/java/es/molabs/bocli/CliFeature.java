package es.molabs.bocli;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.CommandParser;
import es.molabs.bocli.parser.ConsoleCommandParser;
import es.molabs.bocli.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CliFeature {

    private TestOutput output;

    private WireMockServer apiMock;
    private CommandParser<String[]> commandParser;

    @Before
    public void setUp() {
        apiMock = new WireMockServer(8080);
        apiMock.start();

        output = new TestOutput();
        commandParser = new ConsoleCommandParser(output, new WebClient());
    }

    @After
    public void tearDown() {
        if (apiMock.isRunning()) {
            apiMock.stop();
        }
    }

    @Test public void 
    user_can_query_creators_with_a_filter_and_sorting() throws IOException {
        String expectedBody = TestUtils.readFile("/creator/get_two_creators.json");

        apiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/api/creators"))
                    .withQueryParam("comics", WireMock.equalTo("3"))
                    .withQueryParam("sort", WireMock.equalTo("fullName"))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(expectedBody)
                    )
            );

        String[] args = {"-list", "-filter", "comics=3", "-sort", "fullName"};

        commandParser.parse(args).execute();

        Assertions
            .assertThat(output.getLine(0))
            .isEqualToNormalizingNewlines(expectedBody);
    }

    private class TestOutput implements Output {

        private final List<String> lines;

        private TestOutput() {
            this.lines = new LinkedList<>();
        }

        @Override
        public void printLine(String line) {
            lines.add(line);
        }

        public String getLine(int index) {
            return lines.get(index);
        }
    }
}