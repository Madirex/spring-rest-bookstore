package com.nullers.restbookstore.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AuthSingInInvalid
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthSingInInvalid extends AuthException {
    /**
     * Constructor
     *
     * @param message Mensaje de error
     */
    public AuthSingInInvalid(String message) {
        super(message);
    }
}