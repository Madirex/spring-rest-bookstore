package com.nullers.restbookstore.user.exception;

/**
 * Excepcion de usuario
 */
public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
