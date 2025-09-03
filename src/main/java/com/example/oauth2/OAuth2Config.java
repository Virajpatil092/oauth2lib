package com.example.oauth2;


public class OAuth2Config {
    private final String authServerUrl;
    private final String clientId;
    private final String clientSecret;
    private final String scope;


    public OAuth2Config(String authServerUrl, String clientId, String clientSecret, String scope) {
        this.authServerUrl = authServerUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
    }


    public String getAuthServerUrl() { return authServerUrl; }
    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getScope() { return scope; }
}