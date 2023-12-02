package com.nullers.restbookstore.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception para cuando el usuario no existe
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserPasswordsDiffers extends AuthException {
    /**
     * Constructor
     *
     * @param message Mensaje de error
     */
    public UserPasswordsDiffers(String message) {
        super(message);
    }
}
