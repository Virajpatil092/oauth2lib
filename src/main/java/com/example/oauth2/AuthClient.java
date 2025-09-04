package com.example.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthClient {
    private final AuthConfig config;
    private final TokenStore tokenStore;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthClient(AuthConfig config, TokenStore tokenStore) {
        this.config = config;
        this.tokenStore = tokenStore;
    }

    public String getAccessToken() throws Exception {
        TokenResponse token = tokenStore.load();

        if (token == null || token.isExpired()) {
            token = requestNewToken();
            tokenStore.save(token);
        }

        return token.getAccessToken();
    }

    private TokenResponse requestNewToken() throws Exception {
        URL url = new URL(config.getAuthServerUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Basic Auth
        String basicAuth = Base64.getEncoder().encodeToString(
                (config.getUsername() + ":" + config.getPassword()).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Required body
        String body = "grant_type=client_credentials";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() != 200) {
            try (InputStream err = conn.getErrorStream()) {
                String error = new BufferedReader(new InputStreamReader(err))
                        .lines().reduce("", (acc, line) -> acc + line);
                throw new IOException("Token request failed with HTTP " + conn.getResponseCode() + ": " + error);
            }
        }

        try (InputStream is = conn.getInputStream()) {
            return mapper.readValue(is, TokenResponse.class);
        }
    }
}
