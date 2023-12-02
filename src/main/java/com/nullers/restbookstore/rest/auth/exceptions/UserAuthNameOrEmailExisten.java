package com.nullers.restbookstore.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception para cuando el nombre de usuario o el email ya existen en la base de datos
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAuthNameOrEmailExisten extends AuthException {
    public UserAuthNameOrEmailExisten(String message) {
        super(message);
    }
}
