package com.example.security;


import com.example.dao.repository.UserRepository;
import com.example.domain.UserEntity;
import com.example.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("UserEntity not found with email : " + email)
        );

        return UserPrincipal.create(userEntity);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("UserEntity", "id", id)
        );

        return UserPrincipal.create(userEntity);
    }
}