package com.example.security.oauth2;


import com.example.domain.dto.UserServiceRequestDTO;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import com.example.security.oauth2.user.OAuth2OidcUserInfoFactory;
import com.example.security.oauth2.user.OAuth2UserInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomOAuth2OidcUserService extends OidcUserService {

    private final CustomUserService customUserService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            OAuth2UserInfo oAuth2UserInfo = OAuth2OidcUserInfoFactory.getOAuth2OidcUserInfo(userRequest.getClientRegistration().getRegistrationId(), oidcUser.getAttributes());
            if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
                throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
            }
            return customUserService.processOAuth2User(new UserServiceRequestDTO(userRequest.getClientRegistration().getRegistrationId(),oAuth2UserInfo));
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
}
