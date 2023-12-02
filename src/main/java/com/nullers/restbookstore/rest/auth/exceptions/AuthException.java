package com.nullers.restbookstore.rest.auth.exceptions;

/**
 * AuthException
 *
 * @Author Binwei Wang
 */
public abstract class AuthException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message Mensaje de error
     */
    protected AuthException(String message) {
        super(message);
    }
}
