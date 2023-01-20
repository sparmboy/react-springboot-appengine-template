package com.example.security.oauth2;


import static com.example.constants.RoleAndPrivilegeConstants.ROLE_USER;
import static com.example.utils.UserRoleUtils.getAuthorities;

import com.example.dao.repository.UserRepository;
import com.example.dao.repository.UserRoleRepository;
import com.example.domain.UserEntity;
import com.example.domain.UserRoleEntity;
import com.example.domain.dto.UserServiceRequestDTO;
import com.example.domain.enums.AuthProvider;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import com.example.domain.exceptions.ResourceNotFoundException;
import com.example.security.UserPrincipal;
import java.util.Collections;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class CustomUserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public OidcUser processOAuth2User(UserServiceRequestDTO userServiceRequestDTO) {

        Optional<UserEntity> userOptional = userRepository.findByEmail(userServiceRequestDTO.getEmail());
        UserEntity userEntity;
        if(userOptional.isPresent()) {
            userEntity = userOptional.get();
            if(!userEntity.getProvider().equals(AuthProvider.valueOf(userServiceRequestDTO.getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                    userEntity.getProvider() + " account. Please use your " + userEntity.getProvider() +
                    " account to login.");
            }
            userEntity = updateExistingUser(userEntity, userServiceRequestDTO);
        } else {
            userEntity = registerNewUser(userServiceRequestDTO);
        }

        return UserPrincipal.create(userEntity, getAuthorities(userEntity.getRoles()), userServiceRequestDTO.getAttributes());
    }

    private UserEntity registerNewUser(UserServiceRequestDTO userServiceRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setProvider(AuthProvider.valueOf(userServiceRequestDTO.getRegistrationId()));
        userEntity.setProviderId(userServiceRequestDTO.getProviderId());
        userEntity.setName(userServiceRequestDTO.getName());
        userEntity.setEmail(userServiceRequestDTO.getEmail());
        userEntity.setImageUrl(userServiceRequestDTO.getImageUrl());
        userEntity.setRoles(Collections.singletonList(userRoleRepository.findByName(ROLE_USER).orElseThrow(() -> new ResourceNotFoundException(UserRoleEntity.class.getName(), "name", ROLE_USER))));

        return userRepository.save(userEntity);
    }

    private UserEntity updateExistingUser(UserEntity existingUserEntity, UserServiceRequestDTO userServiceRequestDTO) {
        existingUserEntity.setName(userServiceRequestDTO.getName());
        existingUserEntity.setImageUrl(userServiceRequestDTO.getImageUrl());
        return userRepository.save(existingUserEntity);
    }
}
