package com.nullers.restbookstore.user.exception;

public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
