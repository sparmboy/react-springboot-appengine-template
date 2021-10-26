package com.example.domain.exceptions;

public class UserNotAuthenticatedException extends Exception {
    public UserNotAuthenticatedException() {
        super("The is no authenticated user");
    }
}
