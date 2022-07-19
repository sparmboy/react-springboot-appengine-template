package com.example.security.oauth2;

import com.example.dao.repository.UserRepository;
import com.example.domain.UserEntity;
import com.example.domain.enums.AuthProvider;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import com.example.security.UserPrincipal;
import com.example.security.oauth2.user.OAuth2OidcUserInfoFactory;
import com.example.security.oauth2.user.OAuth2UserInfo;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2OidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOAuth2OidcUser(userRequest, oidcUser);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOAuth2OidcUser(OidcUserRequest oidcUserRequest, OidcUser oidcUser) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2OidcUserInfoFactory.getOAuth2OidcUserInfo(oidcUserRequest.getClientRegistration().getRegistrationId(), oidcUser.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<UserEntity> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserEntity userEntity;
        if(userOptional.isPresent()) {
            userEntity = userOptional.get();
            if(!userEntity.getProvider().equals(AuthProvider.valueOf(oidcUserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        userEntity.getProvider() + " account. Please use your " + userEntity.getProvider() +
                        " account to login.");
            }
            userEntity = updateExistingUser(userEntity, oAuth2UserInfo);
        } else {
            userEntity = registerNewUser(oidcUserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(userEntity, oidcUser.getAttributes());
    }

    private UserEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        UserEntity userEntity = new UserEntity();

        userEntity.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        userEntity.setProviderId(oAuth2UserInfo.getId());
        userEntity.setName(oAuth2UserInfo.getName());
        userEntity.setEmail(oAuth2UserInfo.getEmail());
        userEntity.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(userEntity);
    }

    private UserEntity updateExistingUser(UserEntity existingUserEntity, OAuth2UserInfo oAuth2UserInfo) {
        existingUserEntity.setName(oAuth2UserInfo.getName());
        existingUserEntity.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUserEntity);
    }

}
