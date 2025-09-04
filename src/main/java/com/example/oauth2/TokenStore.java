package com.example.oauth2;

public interface TokenStore {
    void save(TokenResponse token) throws Exception;
    TokenResponse load() throws Exception;
}
