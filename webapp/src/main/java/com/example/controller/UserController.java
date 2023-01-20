package com.example.controller;

import com.example.dao.repository.UserRepository;
import com.example.domain.UserEntity;
import com.example.domain.exceptions.ResourceNotFoundException;
import com.example.domain.mappers.UserMapper;
import com.example.model.UserDTO;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/user/me")
    public UserDTO getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userMapper.map(
            userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "id", userPrincipal.getId()))
        );
    }
}