package com.example.oauth2;


public interface TokenStore {
    void save(TokenResponse tokenResponse);
    TokenResponse load();
}