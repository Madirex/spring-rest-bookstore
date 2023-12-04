package com.nullers.restbookstore.rest.user.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción de usuario no encontrado
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends ResponseExceptionNotFound {
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
    public UserNotFound(UUID id) {
        super("Usuario con id" + id + " no encontrado");
    }
}
