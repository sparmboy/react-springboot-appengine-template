package com.example.security.oauth2;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

@Slf4j
public class DynamicInMemoryClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {
    private final Map<String, ClientRegistration> registrations;
    private final Map<String, ClientSecretGenerator> clientSecretGeneratorMap;

    private Map<String,Map.Entry<String,LocalDateTime>> registrationClientSecretExpiryMap = new HashMap<>();


    public DynamicInMemoryClientRegistrationRepository(List<ClientRegistration> clientRegistrations, Map<String, ClientSecretGenerator> clientSecretGeneratorMap) {
        this.clientSecretGeneratorMap = clientSecretGeneratorMap;
        registrations = createRegistrationsMap(clientRegistrations);
    }

    public DynamicInMemoryClientRegistrationRepository(Map<String, ClientRegistration> registrations, Map<String, ClientSecretGenerator> clientSecretGeneratorMap) {
        this.registrations = registrations;
        this.clientSecretGeneratorMap = clientSecretGeneratorMap;
    }


    private static Map<String, ClientRegistration> createRegistrationsMap(List<ClientRegistration> registrations) {
        Assert.notEmpty(registrations, "registrations cannot be empty");
        return toUnmodifiableConcurrentMap(registrations);
    }

    private static Map<String, ClientRegistration> toUnmodifiableConcurrentMap(List<ClientRegistration> registrations) {
        ConcurrentHashMap<String, ClientRegistration> result = new ConcurrentHashMap();
        Iterator var2 = registrations.iterator();

        while (var2.hasNext()) {
            ClientRegistration registration = (ClientRegistration) var2.next();
            Assert.state(!result.containsKey(registration.getRegistrationId()), () -> {
                return String.format("Duplicate key %s", registration.getRegistrationId());
            });
            result.put(registration.getRegistrationId(), registration);
        }

        return Collections.unmodifiableMap(result);
    }


    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "registrationId cannot be empty");

        ClientRegistration immutableClientRegistration = this.registrations.get(registrationId);

        try {
            log.trace("*** Registration: {}", registrationId);
            String clientSecret = getClientSecretForRegistration(immutableClientRegistration,registrationId);
            log.trace("**** {}", clientSecret);

            return ClientRegistration
                .withRegistrationId(registrationId)
                .clientId(immutableClientRegistration.getClientId())
                .authorizationGrantType(immutableClientRegistration.getAuthorizationGrantType())
                .clientName(immutableClientRegistration.getClientName())
                .clientAuthenticationMethod(immutableClientRegistration.getClientAuthenticationMethod())
                .clientSecret(clientSecret)
                .redirectUri(immutableClientRegistration.getRedirectUri())
                .scope(immutableClientRegistration.getScopes())
                .providerConfigurationMetadata(immutableClientRegistration.getProviderDetails().getConfigurationMetadata())
                .issuerUri(immutableClientRegistration.getProviderDetails().getIssuerUri())
                .jwkSetUri(immutableClientRegistration.getProviderDetails().getJwkSetUri())
                .tokenUri(immutableClientRegistration.getProviderDetails().getTokenUri())
                .authorizationUri(immutableClientRegistration.getProviderDetails().getAuthorizationUri())
                .userInfoUri(immutableClientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .userInfoAuthenticationMethod(immutableClientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod())
                .userNameAttributeName(immutableClientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName())
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed while generating client secret: " + e, e);
        }

    }

    private String getClientSecretForRegistration(ClientRegistration immutableClientRegistration, String registrationId) throws Exception {
        // Do we have a client secret generator registered for this registration ID?
        ClientSecretGenerator generator  = clientSecretGeneratorMap.get(registrationId);
        if( generator != null ) {
            // If so has the current client secret for this registration expired?
            Map.Entry<String,LocalDateTime> secretAndExpiryTime = registrationClientSecretExpiryMap.get(registrationId);
            if( secretAndExpiryTime == null || LocalDateTime.now().isAfter(secretAndExpiryTime.getValue())) {
                Map.Entry<String,LocalDateTime> newClientSecret  = clientSecretGeneratorMap.get(registrationId).generateClientSecret();
                registrationClientSecretExpiryMap.put(registrationId, newClientSecret);
                return newClientSecret.getKey();
            } else {
                return secretAndExpiryTime.getKey();
            }
        } else {
            return immutableClientRegistration.getClientSecret();
        }
    }


    public Iterator<ClientRegistration> iterator() {
        return this.registrations.values().iterator();
    }
}
