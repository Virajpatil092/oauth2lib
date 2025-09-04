package com.example.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class FileTokenStore implements TokenStore {
    private final File file;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileTokenStore(String filename) {
        this.file = new File(filename);
    }

    @Override
    public void save(TokenResponse token) throws Exception {
        mapper.writeValue(file, token);
    }

    @Override
    public TokenResponse load() throws Exception {
        if (!file.exists()) return null;
        return mapper.readValue(file, TokenResponse.class);
    }
}
