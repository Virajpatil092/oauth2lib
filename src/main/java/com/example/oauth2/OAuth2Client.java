package com.example.oauth2;


import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class OAuth2Client {
    private final OAuth2Config config;
    private final TokenStore tokenStore;
    private final ObjectMapper mapper = new ObjectMapper();


    public OAuth2Client(OAuth2Config config, TokenStore tokenStore) {
        this.config = config;
        this.tokenStore = tokenStore;
    }


    public String getAccessToken() throws IOException {
        TokenResponse token = tokenStore.load();


        if (token == null || token.isExpired()) {
            if (token != null && token.getRefreshToken() != null) {
                token = refreshToken(token.getRefreshToken());
            } else {
                token = clientCredentialsGrant();
            }
            tokenStore.save(token);
        }


        return token.getAccessToken();
    }


    private TokenResponse clientCredentialsGrant() throws IOException {
        String body = "grant_type=client_credentials&client_id=" +
                URLEncoder.encode(config.getClientId(), "UTF-8") +
                "&client_secret=" + URLEncoder.encode(config.getClientSecret(), "UTF-8") +
                "&scope=" + URLEncoder.encode(config.getScope(), "UTF-8");

        return requestToken(body);
    }

    private TokenResponse refreshToken(String refreshToken) throws IOException {
        String body = "grant_type=refresh_token&refresh_token=" +
                URLEncoder.encode(refreshToken, "UTF-8") +
                "&client_id=" + URLEncoder.encode(config.getClientId(), "UTF-8") +
                "&client_secret=" + URLEncoder.encode(config.getClientSecret(), "UTF-8");

        return requestToken(body);
    }


    private TokenResponse requestToken(String body) throws IOException {
        URL url = new URL(config.getAuthServerUrl() + "/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }


        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Token request failed: " + conn.getResponseCode());
        }


        try (InputStream is = conn.getInputStream()) {
            return mapper.readValue(is, TokenResponse.class);
        }
    }
}