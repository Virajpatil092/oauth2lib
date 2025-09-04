package com.example.oauth2;

public class AuthConfig {
    private final String authServerUrl;
    private final String username;
    private final String password;

    public AuthConfig(String authServerUrl, String username, String password) {
        this.authServerUrl = authServerUrl;
        this.username = username;
        this.password = password;
    }

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
