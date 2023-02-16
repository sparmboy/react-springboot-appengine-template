package com.example.config;

import com.example.security.oauth2.AppleClientSecretGenerator;
import com.example.security.oauth2.ClientSecretGenerator;
import com.example.security.oauth2.DynamicInMemoryClientRegistrationRepository;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration(
    proxyBeanMethods = false
)
@EnableConfigurationProperties({OAuth2ClientProperties.class})
@Conditional({ClientsConfiguredCondition.class})
class OAuthProps {
    OAuthProps() {
    }

    @Bean
    public DynamicInMemoryClientRegistrationRepository clientRegistrations(OAuth2ClientProperties properties, AppleClientSecretGenerator appleClientSecretGenerator) {
       return new DynamicInMemoryClientRegistrationRepository(
           new ArrayList(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values()),
           new HashMap<String, ClientSecretGenerator>(){{
               put("apple",appleClientSecretGenerator);
           }}
       );
    }
}
