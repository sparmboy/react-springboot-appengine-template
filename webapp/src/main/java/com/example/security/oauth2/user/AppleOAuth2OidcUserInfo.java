package com.example.security.oauth2.user;

import java.util.Map;

public class AppleOAuth2OidcUserInfo extends OAuth2UserInfo {

    public AppleOAuth2OidcUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        String name = (String) attributes.get("name");
        return name == null ? getEmail() : name;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
