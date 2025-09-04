package com.example.oauth2;

public class ExampleApp {
    public static void main(String[] args) throws Exception {
        AuthConfig config = new AuthConfig(
                "http://localhost:8080/token", // your token URL
                "myUsername",                  // Basic Auth username
                "myPassword"                   // Basic Auth password
        );

        TokenStore store = new FileTokenStore("tokens.json");
        AuthClient client = new AuthClient(config, store);

        String token = client.getAccessToken();
        System.out.println("Access Token: " + token);
    }
}
