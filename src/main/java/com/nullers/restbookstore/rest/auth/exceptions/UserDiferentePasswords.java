package com.nullers.restbookstore.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception para cuando el usuario no existe
 *
 * @Autor: Binwei Wang
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDiferentePasswords extends AuthException {
    public UserDiferentePasswords(String message) {
        super(message);
    }
}
