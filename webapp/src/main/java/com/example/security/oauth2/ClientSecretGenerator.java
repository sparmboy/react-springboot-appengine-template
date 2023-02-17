package com.example.security.oauth2;

import java.time.LocalDateTime;
import java.util.Map;

public interface ClientSecretGenerator {
    Map.Entry<String, LocalDateTime> generateClientSecret() throws Exception;
}
