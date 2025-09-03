package com.example.oauth2;


public class ExampleApp {
    public static void main(String[] args) throws Exception {
        OAuth2Config config = new OAuth2Config(
                "http://localhost:8080/oauth2",
                "my-client-id",
                "my-client-secret",
                "read write"
        );


        TokenStore store = new FileTokenStore("tokens.json");
        OAuth2Client client = new OAuth2Client(config, store);


        String token = client.getAccessToken();
        System.out.println("Access Token: " + token);
    }
}