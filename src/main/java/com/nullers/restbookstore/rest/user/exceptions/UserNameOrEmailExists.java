package com.nullers.restbookstore.rest.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de usuario no encontrado
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameOrEmailExists extends UserException {
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}
