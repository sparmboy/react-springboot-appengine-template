package com.example.domain.dto;

import com.example.security.oauth2.user.OAuth2UserInfo;
import java.util.Map;
import lombok.Value;

@Value
public class UserServiceRequestDTO {
    String registrationId;
    String providerId;
    String name;
    String email;
    String imageUrl;
    Map<String, Object> attributes;

    public UserServiceRequestDTO(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        this.registrationId = registrationId;
        this.providerId = oAuth2UserInfo.getId();
        this.name = oAuth2UserInfo.getName();
        this.email = oAuth2UserInfo.getEmail();
        this.imageUrl = oAuth2UserInfo.getImageUrl();
        this.attributes = oAuth2UserInfo.getAttributes();
    }
}
