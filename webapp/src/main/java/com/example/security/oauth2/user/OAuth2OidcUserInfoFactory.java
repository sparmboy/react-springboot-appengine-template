package com.example.security.oauth2.user;


import com.example.domain.enums.AuthProvider;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import java.util.Map;

public class OAuth2OidcUserInfoFactory {

    public static OAuth2UserInfo getOAuth2OidcUserInfo(String registrationId, Map<String, Object> attributes) {
         if (registrationId.equalsIgnoreCase(AuthProvider.apple.toString())) {
            return new AppleOAuth2OidcUserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
