package es.molabs.bocli.client;

import com.eclipsesource.json.Json;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.bocli.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RunWith(JUnit4.class)
public class WebClientShould {

    private static final int API_PORT;

    static {
        API_PORT = 20000 + new Random().nextInt(25000);
    }

    private WireMockServer apiMock;
    private WebClient webClient;

    @Before
    public void setUp() {
        apiMock = new WireMockServer(API_PORT);
        apiMock.start();

        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        if (apiMock.isRunning()) {
            apiMock.stop();
        }
    }

    @Test public void
    read_response_body_for_get_requests() throws IOException {
        String expectedBody = TestUtils.readFile("/creator/get_two_creators.json");

        apiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/api/creators"))
                    .withQueryParam("id", WireMock.equalTo("3"))
                    .withQueryParam("fullName", WireMock.equalTo("Some Creator"))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(expectedBody)
                    )
            );

        Map<String, String> queryString = new HashMap<>();
        queryString.put("id", "3");
        queryString.put("fullName", "Some Creator");

        String responseBody = webClient.get("http://localhost:" + API_PORT + "/api/creators", queryString);

        Assertions
            .assertThat(responseBody)
            .isEqualToNormalizingNewlines(expectedBody);
    }

    @Test public void
    read_response_body_for_post_requests() throws IOException {
        String requestBody =
            Json
                .object()
                .add("creatorId", 3)
                .add("note", "Test note")
                .toString();

        String expectedResponseBody =
            Json
                .object()
                .add("id", 1)
                .add("creatorId", 3)
                .add("note", "Test note")
                .toString();

        apiMock
            .stubFor(
                WireMock
                    .post(WireMock.urlPathEqualTo("/api/creator/note"))
                    .withRequestBody(WireMock.equalToJson(requestBody))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(expectedResponseBody)
                    )
            );

        String responseBody = webClient.post("http://localhost:" + API_PORT + "/api/creator/note", requestBody);

        Assertions
            .assertThat(responseBody)
            .isEqualTo(expectedResponseBody);

        apiMock
            .verify(
                WireMock
                    .postRequestedFor(WireMock.urlPathEqualTo("/api/creator/note"))
                    .withRequestBody(WireMock.equalToJson(requestBody))
            );
    }

    @Test public void
    read_response_body_for_put_requests() throws IOException {
        String noteId = "1";

        String requestBody =
            Json
                .object()
                .add("id", 1)
                .add("note", "Test note")
                .toString();

        String expectedResponseBody =
            Json
                .object()
                .add("id", 1)
                .add("creatorId", 3)
                .add("note", "Test note")
                .toString();

        apiMock
            .stubFor(
                WireMock
                    .put(WireMock.urlPathEqualTo("/api/creator/note/" + noteId))
                    .withRequestBody(WireMock.equalToJson(requestBody))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(expectedResponseBody)
                    )
            );

        String responseBody = webClient.put("http://localhost:" + API_PORT + "/api/creator/note", noteId, requestBody);

        Assertions
            .assertThat(responseBody)
            .isEqualTo(expectedResponseBody);

        apiMock
            .verify(
                WireMock
                    .putRequestedFor(WireMock.urlPathEqualTo("/api/creator/note/" + noteId))
                    .withRequestBody(WireMock.equalToJson(requestBody))
            );
    }

    @Test public void
    process_delete_requests() throws IOException {
        String noteId = "1";
        String expectedResponseBody = "";

        apiMock
            .stubFor(
                WireMock
                    .delete(WireMock.urlPathEqualTo("/api/creator/note/" + noteId))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(expectedResponseBody)
                    )
            );

        String responseBody = webClient.delete("http://localhost:" + API_PORT + "/api/creator/note", noteId);

        Assertions
            .assertThat(responseBody)
            .isEqualTo(expectedResponseBody);

        apiMock
            .verify(
                WireMock
                    .deleteRequestedFor(WireMock.urlPathEqualTo("/api/creator/note/" + noteId))
                    .withHeader("Content-Type", WireMock.equalTo("application/json"))
            );
    }

    @Test(expected = InvalidResponseException.class) public void
    throw_exception_is_response_code_is_not_valid() throws IOException {
        String noteId = "1";

        apiMock
            .stubFor(
                WireMock
                    .delete(WireMock.urlPathEqualTo("/api/creator/note/" + noteId))
                    .willReturn(
                        WireMock
                            .aResponse()
                            .withStatus(400)
                            .withHeader("Content-Type", "application/json")
                    )
            );

        webClient.delete("http://localhost:" + API_PORT + "/api/creator/note", noteId);
    }
}