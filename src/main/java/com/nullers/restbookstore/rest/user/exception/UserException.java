package com.nullers.restbookstore.rest.user.exception;

/**
 * Excepcion de usuario
 */
public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
