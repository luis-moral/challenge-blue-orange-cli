package es.molabs.bocli.client;


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

@RunWith(JUnit4.class)
public class WebClientShould {

    private WireMockServer apiMock;
    private WebClient webClient;

    @Before
    public void setUp() {
        apiMock = new WireMockServer(8080);
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
    return_body_for_get_requests() throws IOException {
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

        String body = webClient.get("http://localhost:8080/api/creators", queryString);

        Assertions
            .assertThat(body)
            .isEqualToNormalizingNewlines(expectedBody);
    }
}