package com.example.oauth2;


import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;


public class FileTokenStore implements TokenStore {
    private final File file;
    private final ObjectMapper mapper = new ObjectMapper();


    public FileTokenStore(String filename) {
        this.file = new File(filename);
    }


    @Override
    public void save(TokenResponse tokenResponse) {
        try {
            mapper.writeValue(file, tokenResponse);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save token", e);
        }
    }


    @Override
    public TokenResponse load() {
        if (!file.exists()) return null;
        try {
            return mapper.readValue(file, TokenResponse.class);
        } catch (IOException e) {
            return null;
        }
    }
}