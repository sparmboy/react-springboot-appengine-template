package com.example.security.oauth2;


import com.example.domain.dto.UserServiceRequestDTO;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import com.example.security.oauth2.user.OAuth2UserInfo;
import com.example.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CustomUserService customUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
            if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
                throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
            }
            return customUserService.processOAuth2User(new UserServiceRequestDTO(oAuth2UserRequest.getClientRegistration().getRegistrationId(),oAuth2UserInfo));
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
}
