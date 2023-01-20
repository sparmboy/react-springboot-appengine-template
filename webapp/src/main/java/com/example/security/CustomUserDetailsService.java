package com.example.security;


import static com.example.utils.UserRoleUtils.getAuthorities;

import com.example.dao.repository.UserRepository;
import com.example.domain.UserEntity;
import com.example.domain.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException(UserEntity.class.getName(), "email", email)
            );

        return UserPrincipal.create(userEntity,getAuthorities(userEntity.getRoles()));
    }

    public UserDetails loadUserById(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(UserEntity.class.getName(), "id", id)
        );

        return UserPrincipal.create(userEntity,getAuthorities(userEntity.getRoles()));
    }
}