package com.epam.jpop.userservice.exception;

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
    }

    public UserIdMismatchException(String message, Throwable t) {
        super(message, t);
    }
}
