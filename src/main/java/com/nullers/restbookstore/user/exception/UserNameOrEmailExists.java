package com.nullers.restbookstore.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion de usuario no encontrado
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameOrEmailExists extends UserException {
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}
