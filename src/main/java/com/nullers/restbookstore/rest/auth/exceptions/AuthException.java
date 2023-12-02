package com.nullers.restbookstore.rest.auth.exceptions;

/**
 * AuthException
 *
 * @Author Binwei Wang
 */
public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
