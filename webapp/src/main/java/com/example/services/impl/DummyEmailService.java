package com.example.services.impl;

import com.example.model.SignupRequest;
import com.example.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DummyEmailService implements EmailService {
    @Override
    public void sendVerificationEmail(SignupRequest signupRequest, String url) {
        throw new RuntimeException("Verification emails not implemented!");
    }
}
