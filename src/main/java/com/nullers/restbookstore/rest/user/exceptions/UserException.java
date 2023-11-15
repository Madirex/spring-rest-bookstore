package com.nullers.restbookstore.rest.user.exceptions;

/**
 * Excepción de usuario
 */
public abstract class UserException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message mensaje de error
     */
    UserException(String message) {
        super(message);
    }
}