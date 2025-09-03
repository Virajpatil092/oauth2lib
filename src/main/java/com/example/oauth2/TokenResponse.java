package com.example.oauth2;


import com.fasterxml.jackson.annotation.JsonProperty;


public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;


    @JsonProperty("refresh_token")
    private String refreshToken;


    @JsonProperty("expires_in")
    private long expiresIn;


    private long createdAt = System.currentTimeMillis();


    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public long getExpiresIn() { return expiresIn; }


    public boolean isExpired() {
        return System.currentTimeMillis() > (createdAt + (expiresIn - 30) * 1000);
    }
}