package com.nullers.restbookstore.rest.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de usuario no encontrado
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {
    /**
     * Constructor
     *
     * @param message mensaje de error
     */
    public UserNotFound(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param id id del usuario
     */
    public UserNotFound(Long id) {
        super("Usuario con id" + id + "no encontrado");
    }
}
