package com.nullers.restbookstore.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion de usuario no encontrado
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(Long id) {
        super("Usuario con id" + id + "no encontrado");
    }
}
