package com.epam.jpop.userservice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
