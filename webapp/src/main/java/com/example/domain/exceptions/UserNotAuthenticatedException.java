package com.example.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthenticatedException extends Exception {
    public UserNotAuthenticatedException() {
        super("The is no authenticated user");
    }
}
