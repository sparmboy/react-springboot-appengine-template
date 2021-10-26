package com.example.controller;

import com.example.dao.repository.UserRepository;
import com.example.domain.UserEntity;
import com.example.domain.exceptions.ResourceNotFoundException;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserEntity", "id", userPrincipal.getId()));
    }
}