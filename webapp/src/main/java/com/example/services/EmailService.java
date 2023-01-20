package com.example.services;

import com.example.model.SignupRequest;

public interface EmailService {
    /**
     * Implementations should send an email to the user to allow them to verify their email address
     * @param signupRequest Details of the user that is signing up
     * @param url   The URL that should be embedded in the email for the user to click that will verify their address
     */
    void sendVerificationEmail(SignupRequest signupRequest, String url) ;
}
