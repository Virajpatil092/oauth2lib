package com.example.oauth2;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OAuth2ClientTest {
    private WireMockServer server;

    @Before
    public void setup() {
        server = new WireMockServer(8089);
        server.start();
        WireMock.configureFor("localhost", 8089);
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void testClientCredentialsFlowSuccess() throws Exception {
        WireMock.stubFor(WireMock.post("/token")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"abc123\",\"refresh_token\":\"ref123\",\"expires_in\":3600}")));

        OAuth2Config config = new OAuth2Config("http://localhost:8089", "id", "secret", "read");
        FileTokenStore store = new FileTokenStore("test_tokens.json");
        OAuth2Client client = new OAuth2Client(config, store);

        String token = client.getAccessToken();
        assertEquals("abc123", token);
    }

    @Test
    public void testClientCredentialsFlowFail() throws Exception {
        WireMock.stubFor(WireMock.post("/token")
                .willReturn(WireMock.aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"invalid_client\"}")));

        OAuth2Config config = new OAuth2Config("http://localhost:8089", "id", "secret", "read");
        FileTokenStore store = new FileTokenStore("test_tokens.json");
        OAuth2Client client = new OAuth2Client(config, store);

        try {
            client.getAccessToken();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Token request failed: 400"));
        }
    }
}
