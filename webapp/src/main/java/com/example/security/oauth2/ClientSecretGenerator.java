package com.example.security.oauth2;

public interface ClientSecretGenerator {
    String generateClientSecret() throws Exception;
}
