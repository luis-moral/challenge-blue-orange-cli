package es.molabs.bocli;

import com.eclipsesource.json.Json;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.CreateNoteCommand;
import es.molabs.bocli.command.DeleteNoteCommand;
import es.molabs.bocli.command.EditNoteCommand;
import es.molabs.bocli.command.ListCreatorsCommand;
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
    user_can_view_cli_help() throws IOException {
        String expectedOutput = TestUtils.readFile("/help/help.output");

        String[] args = {"-help"};

        commandParser.parse(args).execute();

        Assertions
            .assertThat(output.getLine(0))
            .isEqualToIgnoringNewLines(expectedOutput);
    }

    @Test public void 
    user_can_query_creators_with_a_filter_and_sorting() throws IOException {
        String expectedBody = TestUtils.readFile("/creator/get_two_creators.json");

        apiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo(ListCreatorsCommand.PATH_CREATORS))
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

    @Test public void
    user_can_create_edit_and_delete_creator_notes() {
        int id = 1;
        String createNoteResponse =
            Json
                .object()
                .add("id", id)
                .add("creatorId", 5)
                .add("note", "Some text")
                .toString();

        String editNoteResponse =
            Json
                .object()
                .add("note", "Other text")
                .toString();

        apiMock
            .stubFor(
                WireMock
                    .post(WireMock.urlPathEqualTo(CreateNoteCommand.PATH_CREATOR_NOTE))
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json")
                                .withBody(createNoteResponse)
                    )
            );

        apiMock
            .stubFor(
                WireMock
                    .put(WireMock.urlPathEqualTo(EditNoteCommand.PATH_CREATOR_NOTE + "/" + id))
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(editNoteResponse)
                    )
            );

        apiMock
            .stubFor(
                WireMock
                    .delete(WireMock.urlPathEqualTo(DeleteNoteCommand.PATH_CREATOR_NOTE + "/" + id))
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                    )
            );


        String[] createNoteArgs = {"-add_note", "-creatorId", "5", "-note", "Some text"};
        String[] editNoteArgs = {"-edit_note", "-id", "1", "-note", "Other text"};
        String[] deleteNoteArgs = {"-delete_note", "-id", "1"};

        commandParser.parse(createNoteArgs).execute();
        commandParser.parse(editNoteArgs).execute();
        commandParser.parse(deleteNoteArgs).execute();

        Assertions
            .assertThat(output.getLine(0))
            .isEqualToNormalizingNewlines(createNoteResponse);

        Assertions
            .assertThat(output.getLine(1))
            .isEqualToNormalizingNewlines(editNoteResponse);

        Assertions
            .assertThat(output.getLine(2))
            .isEqualToNormalizingNewlines("");
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